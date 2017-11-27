<?php

require('db.php');
$sql_statement;
$create_statement = 'INSERT INTO Instructor (first_name, last_name, gender) VALUES (?, ?, ?)';
$read_all_statement = 'SELECT instructor_id, first_name, last_name, gender FROM Instructor';
$update_statement = 'UPDATE Instructor SET first_name = ?, last_name = ?, gender = ? WHERE instructor_id = ?';
$delete_statement = 'DELETE FROM Instructor WHERE instructor_id = ?';

/*
 * Returns if the agent is authenticated and an admin, else exits.
 */
function auth_precheck() {
  $sid = $_GET['sid'];
  if ($sid != '') {
    session_id($_GET['sid']);
    session_start();
    $control = $_SESSION['my_control'];
    if ($control == '0') {
      return;
    }
  }
  echo json_encode(array(:error => "Not authorized."));
  exit();
}

$verb = $_GET['verb'];
if ($verb == 'CREATE') {
  auth_precheck();
  $sql_statement = $create_statement;
  $first_name = $_GET['first_name'];
  $last_name = $_GET['last_name'];
  $gender = $_GET['gender'];
  $arguments = array($first_name, $last_name, $gender);
} else if ($verb == 'READ') {
  $sql_statement = $read_all_statement;
  $arguments = array();
} else if ($verb == 'UPDATE') {
  auth_precheck();
  $sql_statement = $update_statement;
  $instructor_id = $_GET['id'];
  $first_name = $_GET['first_name'];
  $last_name = $_GET['last_name'];
  $gender = $_GET['gender'];
  $arguments = array($first_name, $last_name, $gender, $instructor_id);
} else if ($verb == 'DELETE') {
  auth_precheck();
  $sql_statement = $delete_statement;
  $instructor_id = $_GET['id'];
  $arguments = array($instructor_id);
} else {
  // unknown verb
  echo json_encode(array(:error => "Unknown verb"));
  exit();
}

$statement = $dbh->prepare($sql_statement);
$statement->execute(arguments);
$results = $statement->fetchAll();

echo json_encode($results);

?>
