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
$first_name = $_GET['my_first_name'];
$last_name = $_GET['my_last_name'];
$gender = $_GET['my_gender'];
$account_check;
$account_add;

try {
    #make a new DB object to interact with
    $db = new PDO($dsn, $username, $password);
    // user register
    if ($control == 0) {
        #build a SQL statement to query the DB
        $account_check = "SELECT email FROM Student WHERE email = '$email'";
    // admin register
    } else {
        #build a SQL statement to query the DB
        $account_check = "SELECT email FROM Admin WHERE email = '$email'";
    }
    
    #make a query object
    $user_query = $db->query($account_check);
    
    // run the query on the DB
    $users = $user_query->fetchAll(PDO::FETCH_ASSOC);
    
    if ($users) {
        //user exists
        print "false";
    } else {
        // user does not exist
        if ($control == 0) {
            $account_add = "INSERT INTO Student VALUES ('$email', '$pass', '$first_name', '$last_name', '$gender')";
        } else {
            $account_add = "INSERT INTO Admin VALUES ('$email', '$pass', '$first_name', '$last_name')";
        }
        $db->query($account_add);
        
        print true;
    }
   // echo json_encode($result);
    $db = null;
} catch (PDOException $e) {
    $error_message = $e->getMessage();
    $result = array("code"=>300, "message"=>"There was an error connecting to the database: $error_message");
    echo json_encode($result);
    exit();
}
?>
