<?php

ini_set('display_errors', '1');
error_reporting(E_ALL);

require('db.php');
$sql_statement;
$create_statement = 'INSERT INTO Instructor
                       (email, phone_number,
                       position_title, department_name,
                       first_name, last_name)
                     VALUES (?, ?, ?, ?, ?, ?)';
$read_all_statement = 'SELECT * FROM Instructor';
$update_statement = 'UPDATE Instructor SET
                       first_name = ?,
                       last_name = ?,
                       phone_number = ?,
                       position_title = ?,
                       department_name = ?
                     WHERE email = ?';
$delete_statement = 'DELETE FROM Instructor WHERE email = ?';

/*
 * Returns if the agent is authenticated and an admin, else exits.
 */
function auth_precheck() {
  if (isset($_GET['sid'])) {
    $sid = $_GET['sid'];
    if ($sid != '') {
      session_id($sid);
      session_start();
      if (isset($_SESSION['is_admin']) && $_SESSION['is_admin'] == '1') {
        return;
      }
    }
  }
  echo json_encode(array("error" => "Not authorized."));
  exit();
}

if (!isset($_GET['verb'])) {
  $verb = 'UNKNOWN';
} else {
  $verb = $_GET['verb'];
}

if ($verb == 'CREATE') {
  auth_precheck();
  $sql_statement = $create_statement;
  $first_name = $_GET['first_name'];
  $last_name = $_GET['last_name'];
  $position_title = $_GET['position_title'];
  $department_name = $_GET['department_name'];
  $phone_number = $_GET['phone_number'];
  $email = $_GET['email'];
  $arguments = array($email, $phone_number,
    $position_title, $department_name,
    $first_name, $last_name);
} else if ($verb == 'READ') {
  $sql_statement = $read_all_statement;
  $arguments = array();
} else if ($verb == 'UPDATE') {
  auth_precheck();
  $sql_statement = $update_statement;
  $first_name = $_GET['first_name'];
  $last_name = $_GET['last_name'];
  $position_title = $_GET['position_title'];
  $department_name = $_GET['department_name'];
  $phone_number = $_GET['phone_number'];
  $email = $_GET['email'];

  $arguments = array(
    $first_name, $last_name,
    $phone_number,
    $position_title, $department_name,
    $email);
} else if ($verb == 'DELETE') {
  auth_precheck();
  $sql_statement = $delete_statement;
  $email = $_GET['email'];
  $arguments = array($email);
} else {
  /* unknown verb */
  echo json_encode(array("error" => "Unknown verb"));
  exit();
}

$statement = $db->prepare($sql_statement);
$statement->execute($arguments);
$results = $statement->fetchAll(PDO::FETCH_ASSOC);

echo json_encode($results);

?>
