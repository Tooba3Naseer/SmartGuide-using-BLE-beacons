<?php 
 

	include_once 'DbConnect.php';

	$response = array();
	
	if(isset($_GET['apicall'])){
		
		switch($_GET['apicall']){
			
			case 'signup':
				if(isTheseParametersAvailable(array('name','email','password'))){
					$username = $_POST['name']; 
					$email = $_POST['email']; 
					$password = md5($_POST['password']);
					
					
					$stmt = $connect->prepare("SELECT id FROM admin WHERE name = ? OR email = ?");
					$stmt->bind_param("ss", $username, $email);
					$stmt->execute();
					$stmt->store_result();
					
					if($stmt->num_rows > 0){
						$response['error'] = true;
						$response['message'] = 'User already registered';
						$stmt->close();
					}else{
						$stmt = $connect->prepare("INSERT INTO admin (name, email, password) VALUES (?, ?, ?)");
						$stmt->bind_param("ssss", $username, $email, $password);
 
						if($stmt->execute()){
							$stmt = $connect->prepare("SELECT id, name, email, password FROM admin WHERE email = ?"); 
							$stmt->bind_param("s",$username);
							$stmt->execute();
							$stmt->bind_result($id, $username, $email, $password);
							$stmt->fetch();
							
							$user = array(
								'id'=>$id, 
								'name'=>$username, 
								'email'=>$email,
								'password' =>$password
							);
							
							$stmt->close();
							
							$response['error'] = false; 
							$response['message'] = 'User registered successfully'; 
							$response['user'] = $user; 
						}
					}
					
				}else{
					$response['error'] = true; 
					$response['message'] = 'required parameters are not available'; 
				}
				
			break; 
			
			case 'login':
				
				if(isTheseParametersAvailable(array('name', 'password'))){
					
					$username = $_POST['name'];
					$password = md5($_POST['password']); 
					
					$stmt = $connect->prepare("SELECT id, name, email FROM admin WHERE name = ? AND password = ?");
					$stmt->bind_param("ss",$username, $password);
					
					$stmt->execute();
					
					$stmt->store_result();
					
					if($stmt->num_rows > 0){
						
						$stmt->bind_result($id, $username, $email, $gender);
						$stmt->fetch();
						
						$user = array(
							'id'=>$id, 
							'name'=>$username, 
							'email'=>$email
						);
						
						$response['error'] = false; 
						$response['message'] = 'Login successfull'; 
						$response['user'] = $user; 
					}else{
						$response['error'] = false; 
						$response['message'] = 'Invalid username or password';
					}
				}
			break; 
			
			default: 
				$response['error'] = true; 
				$response['message'] = 'Invalid Operation Called';
		}
		
	}else{
		$response['error'] = true; 
		$response['message'] = 'Invalid API Call';
	}
	
	echo json_encode($response);
	
	function isTheseParametersAvailable($params){
		
		foreach($params as $param){
			if(!isset($_POST[$param])){
				return false; 
			}
		}
		return true; 
	}
?>