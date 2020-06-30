<?php
    define("DB_HOST", "localhost");
    define("DB_USER", "root");
    define("DB_PASSWORD", "");
    define("DB_NAME", "smartguide");
	

   
  class DbConnect{
     private $connect;
     function connect()
     {
      $this->connect = mysqli_connect(DB_HOST, DB_USER, DB_PASSWORD, DB_NAME);
		  // $conn = new mysqli($servername, $username, $password, $database);
 
         //if there is some error connecting to the database
          //with die we will stop the further execution by displaying a message causing the error 
         if ($this->connect->connect_error) {
        die("Connection failed: " . $this->connect->connect_error);
       }
		   
        return $this->connect;	
     }
  }      
            

   ?>

