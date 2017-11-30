<?php

ini_set('display_errors', '1');
error_reporting(E_ALL);

require('db.php');
$sql_statement;
$create_statement = "INSERT INTO Rating (instructor_email, student_email, score, hotness, comment, date)
                     VALUES (?, ?, ?, ?, ?, ?, NOW())";
$read_statement = 'SELECT Rating.*, Student.first_name AS author_first_name, Student.last_name AS author_last_name FROM Rating
                   JOIN Student ON Rating.student_email = Student.email
                   WHERE instructor_email = ? AND student_email = ?';
$read_all_statement = 'SELECT Rating.*, Student.first_name AS author_first_name, Student.last_name AS author_last_name
                       FROM Rating
                       JOIN Student ON Rating.student_email = Student.email
                       WHERE instructor_email = ?';
$create_or_update_statement = 'REPLACE INTO Rating SET score = ?, hotness = ?, comment = ?, date = NOW(), instructor_email = ?, student_email = ?';
$delete_statement = 'DELETE FROM Rating WHERE instructor_email = ? AND student_email = ?';

/**
 * Exits the application if the agent does not have a session
 */
function auth_precheck() {
  if (isset($_GET['sid'])) {
    $sid = $_GET['sid'];
    if ($sid != '') {
      session_id($sid);
      session_start();
      if (isset($_SESSION['email'])) {
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

auth_precheck();

$is_admin = $_SESSION['is_admin'] == '1';

if ($verb == 'CREATE') {
  $sql_statement = $create_statement;
  $instructor_email = $_GET['instructor_email'];
  $student_email = $is_admin ? $_GET['student_email'] : $_SESSION['email'];
  $score = $_GET['score'];
  $hotness = $_GET['hotness'];
  $comment = $_GET['comment'];
  $arguments = array($instructor_email, $student_email, $score, $hotness, $comment);
} else if ($verb == 'READ_ALL') {
  $sql_statement = $read_all_statement;
  $instructor_email = $_GET['instructor_email'];
  $arguments = array($instructor_email);
} else if ($verb == 'READ') {
  $sql_statement = $read_statement;
  $instructor_email = $_GET['instructor_email'];
  $student_email = $is_admin ? $_GET['student_email'] : $_SESSION['email'];
  $arguments = array($instructor_email, $student_email);
} else if ($verb == 'CREATE_OR_UPDATE') {
  $sql_statement = $create_or_update_statement;
  $instructor_email = $_GET['instructor_email'];
  $student_email = $is_admin ? $_GET['student_email'] : $_SESSION['email'];
  $score = $_GET['score'];
  $hotness = $_GET['hotness'];
  $comment = $_GET['comment'];
  $arguments = array($score, $hotness, $comment, $instructor_email, $student_email);
} else if ($verb == 'DELETE') {
  $sql_statement = $delete_statement;
  $instructor_email = $_GET['instructor_email'];
  $student_email = $is_admin ? $_GET['student_email'] : $_SESSION['email'];
  $arguments = array($instructor_email, $student_email);
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
