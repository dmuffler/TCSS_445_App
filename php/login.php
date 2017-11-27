<?php
ini_set('display_errors', '1');
error_reporting(E_ALL);

require('db.php');

function login($db, $email, $password) {
  $sql_statement = "
    SELECT email, first_name, last_name, is_admin
    FROM (
      SELECT email, first_name, last_name, password, FALSE AS is_admin
      FROM Student
      UNION
      SELECT email, first_name, last_name, password, TRUE AS is_admin
      FROM Admin
    ) AS Users
    WHERE email = ? AND password = SHA(?);";
    $arguments = array($email, $password);
    $statement = $db->prepare($sql_statement);

    $statement->execute($arguments);
    $results = $statement->fetchAll(PDO::FETCH_ASSOC);

    if (count($results) == 1) {
      $sid = uniqid();
      session_id($sid);
      session_start();

      $result = $results[0];
      $result['sid'] = $sid;

      $_SESSION['email'] = $result['email'];
      $_SESSION['first_name'] = $result['first_name'];
      $_SESSION['last_name'] = $result['last_name'];
      $_SESSION['is_admin'] = $result['is_admin'];

      return $result;
    } else {
      return array("error" => "Email or password not found.");
    }
}

try {
  if (!isset($_GET['email']) || !isset($_GET['password'])) {
    echo json_encode(array('error' => 'Missing parameter.'));
    exit();
  }

  $email = $_GET['email'];
  $password = $_GET['password'];

  $result = login($db, $email, $password);

  if (!$result) {
    echo json_encode(array("error" => "Failed to sign in."));
  } else {
    echo json_encode($result);
  }

} catch (PDOException $e) {
    $error_message = $e->getMessage();
    $result = array("code"=>300, "message"=>"There was an error connecting to the database: $error_message");
    echo json_encode($result);
}
?>
