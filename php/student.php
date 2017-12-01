<?php

ini_set('display_errors', '1');
error_reporting(E_ALL);

require('db.php');
$sql_statement;
$read_statement = 'SELECT email, first_name, last_name FROM Student
                   WHERE email = ?';
$read_all_statement = 'SELECT email, first_name, last_name FROM Student';
$update_statement = 'UPDATE Student SET
                       first_name = ?,
                       last_name = ?
                     WHERE email = ?';
$update_and_password_statement = 'UPDATE Student SET
                       password = SHA(?),
                       first_name = ?,
                       last_name = ?
                     WHERE email = ?';
$delete_statement = 'DELETE FROM Student WHERE email = ?';
$search_statement = 'SELECT email, first_name, last_name FROM Student
                     WHERE
                       first_name LIKE "%"?"%"
                       OR last_name LIKE "%"?"%"
                       OR email LIKE "%"?"%"';

/*
 * Returns if the agent is authenticated and an admin, else exits.
 */
function admin_auth_precheck() {
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

/*
 * Returns if the agent is authenticated and a student, else exits.
 */
function student_auth_precheck() {
  if (isset($_GET['sid'])) {
    $sid = $_GET['sid'];
    if ($sid != '') {
      session_id($sid);
      session_start();
      if (isset($_SESSION['is_admin']) && $_SESSION['is_admin'] == '0') {
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

if ($verb == 'READ') {
  student_auth_precheck();
  $sql_statement = $read_statement;
  $email = $_SESSION['email'];
  $arguments = array($email);
} else if ($verb == 'READ_ALL') {
  admin_auth_precheck();
  $sql_statement = $read_all_statement;
  $arguments = array();
} else if ($verb == 'SEARCH') {
  admin_auth_precheck();
  $query = $_GET['query'];
  $sql_statement = $search_statement;
  $arguments = array($query, $query, $query);
} else if ($verb == 'UPDATE') {
  student_auth_precheck();
  $first_name = $_GET['first_name'];
  $last_name = $_GET['last_name'];
  $email = $_SESSION['email'];

  if (!isset($_GET['password']) || $_GET['password'] === '') {
    $sql_statement = $update_statement;
    $arguments = array(
      $first_name, $last_name, $email);
  } else {
    $password = $_GET['password'];
    $sql_statement = $update_and_password_statement;
    $arguments = array(
      $password, $first_name, $last_name, $email);
  }
} else if ($verb == 'DELETE') {
  admin_auth_precheck();
  $sql_statement = $delete_statement;
  $email = $_GET['email'];
  $arguments = array($email);
} else {
  /* unknown verb */
  echo json_encode(array("error" => "Unknown verb"));
  exit();
}

$statement = $db->prepare($sql_statement);
$success = $statement->execute($arguments);
if ($success) {
  $results = $statement->fetchAll(PDO::FETCH_ASSOC);
} else {
  echo json_encode(array("error" => "Unknown failure"));
}

echo json_encode($results);

?>
