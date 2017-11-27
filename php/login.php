<?php
ini_set('display_errors', '1');
error_reporting(E_ALL);

// connect to the Database
$dsn = 'mysql:host=localhost;dbname=dmuffler';
$username = 'dmuffler';
$password = 'Dawckyun';

// 0 if a student, else an admin.
$control = $_GET['my_control'];

$email = $_GET['my_email'];
$pass = $_GET['my_pass'];
$account_check;

try {
    #make a new DB object to interact with
    $db = new PDO($dsn, $username, $password);
    // student account check
    if ($control == 0) {
        #build a SQL statement to query the DB
        $account_check = "SELECT email, password FROM Student WHERE email = '$email'";
        // admin account check
    } else {
        #build a SQL statement to query the DB
        $account_check = "SELECT email, password FROM Admin WHERE email = '$email'";
    }
    
    #make a query object
    $user_query = $db->query($account_check);

    // run the query on the DB
    $users = $user_query->fetchAll(PDO::FETCH_ASSOC);
    
    // correct email and pass
    if (!strcmp($users[0]['email'], $email) && !strcmp($pass, $users[0]['password'])) {
        print "true";
    // incorrect email or password
    } else {
        print "false";
    }
    
    $db = null;
} catch (PDOException $e) {
    $error_message = $e->getMessage();
    $result = array("code"=>300, "message"=>"There was an error connecting to the database: $error_message");
    echo json_encode($result);
    exit();
}
?>
