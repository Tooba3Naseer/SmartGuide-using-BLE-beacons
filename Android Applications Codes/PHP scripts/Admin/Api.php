<?php 
 

	include_once 'DbConnect.php';
	$upload_path ='uploads/';
	//Getting the server ip 
	$server_ip = gethostbyname(gethostname());
	
	//creating the upload url 
	$upload_url = 'http://'.$server_ip.'/Admin/'.$upload_path; 
	$response = array();

	if(isset($_GET['apicall'])){
		
		switch($_GET['apicall']){
			
			case 'signup':
				if(isTheseParametersAvailable(array('name','email','password'))){
					$name = $_POST['name']; 
					$email = $_POST['email']; 
					$password = $_POST['password'];
					
					
					$stmt = $connect->prepare("SELECT id FROM admin WHERE name = ? OR email = ?");
					$stmt->bind_param("ss", $name, $email);
					$stmt->execute();
					$stmt->store_result();
						
					if($stmt->num_rows > 0){
						$response['error'] = true;
						$response['message'] = 'User already registered';
						$stmt->close();
					}else{
						$stmt = $connect->prepare("INSERT INTO admin (name, email, password) VALUES (?, ?, ?)");
						$stmt->bind_param("sss", $name, $email, $password);

						if($stmt->execute()){
							$stmt = $connect->prepare("SELECT id, name, email, password FROM admin WHERE email = ?"); 
							$stmt->bind_param("s",$name);
							$stmt->execute();
							$stmt->bind_result($id, $name, $email, $password);
							$stmt->fetch();
							
							$user = array(
								'id'=>$id, 
								'name'=>$name, 
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
				
				if(isTheseParametersAvailable(array('email','password'))){
					
					$email = $_POST['email'];
					$password = $_POST['password']; 
					
					$stmt = $connect->prepare("SELECT id, name, email, password FROM admin WHERE email = ? AND password = ?");
					$stmt->bind_param("ss",$email , $password);
					
					$stmt->execute();
					
					$stmt->store_result();
					
					if($stmt->num_rows > 0){
						
						$stmt->bind_result($id, $name, $email, $password);
						$stmt->fetch();
						
						$user = array(
							'id'=>$id, 
							'name'=>$name, 
							'email'=>$email
						);
						
						$response['error'] = false; 
						$response['message'] = 'Login successfull'; 
						$response['user'] = $user; 
					}else{
						$response['error'] = true; 
						$response['message'] = 'Invalid email or password';
					}
				}
			break;

            case 'createroom':
                if(isTheseParametersAvailable(array('Name','RoomNo','Description','audioInfo','LeftR_id','RightR_id','FrontR_id','BackR_id'))){
                      $roomno = $_POST['RoomNo'];                   
                      $roomname = $_POST['Name'];
                      $description = $_POST['Description'];
					  $audio = $_POST['audioInfo'];
					  $FrontRno = $_POST['FrontR_id'];
					  $BackRno =$_POST['BackR_id'];
					  $LeftRno =$_POST['LeftR_id'];
					  $RightRno = $_POST['RightR_id'];
                        $stmt = $connect->prepare("INSERT INTO roominfo(Name,RoomNo,Description,audioInfo,LeftR_id,RightR_id,FrontR_id,BackR_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
                        $stmt->bind_param("ssssssss", $roomname, $roomno, $description, $audio, $LeftRno, $RightRno, $FrontRno, $BackRno);
                        $stmt->execute();
				    
			          
                        $response['error'] = false;
                        $response['message'] = 'Room Added';
				}else{
                    $response['error'] = true;
                    $response['message'] = 'required parameters are not available';
                }
            break;						
            case 'createroommember':
                if(isTheseParametersAvailable(array('rmroomno','rmperson','rmdesignation','rmexperts'))){
                    $rmroomno = $_POST['rmroomno'];
                    $rmperson = $_POST['rmperson'];

                    $rmdesig = $_POST['rmdesignation'];
                    $rmexperts = $_POST['rmexperts'];

                    $stmt = $connect->prepare("INSERT INTO roommembers(rmroomno, rmperson, rmdesignation,rmexperts) VALUES (?, ?, ?,?)");
                    $stmt->bind_param("ssss", $rmroomno, $rmperson, $rmdesig,$rmexperts);

                    $stmt->execute();
                    $response['error'] = false;
                    $response['message'] = 'Room Member Added';

                }else{
                    $response['error'] = false;
                    $response['message'] = 'required parameters are not available';
                }
            break;
            case 'createofficehours':
                if(isTheseParametersAvailable(array('tmday','tmopen','tmclose','stfmemName'))){
                    $tmday = $_POST['tmday'];
                    $tmopen = $_POST['tmopen'];

                    $tmclose = $_POST['tmclose'];
					$name = $_POST['stfmemName'];
                    $stmt = $connect->prepare("INSERT INTO officetimings(tmday,tmopen,tmclose,stfmemName) VALUES (?, ?, ?,?)");
                    $stmt->bind_param("ssss", $tmday, $tmopen, $tmclose, $name);

                    $stmt->execute();
                    $response['error'] = false;
                    $response['message'] = 'Timings Added';

                }else{
                    $response['error'] = true;
                    $response['message'] = 'required parameters are not available';
                }
            break;

			case 'room1':
				
				if(isTheseParametersAvailable(array('id'))){
					
					$id = $_POST['id'];
					$stmt = $connect->prepare("SELECT id, Name, RoomNo, Description from roominfo WHERE id = ?");
		
                    $stmt->bind_param("i", $id );
							
					$stmt->execute();
					
					$stmt->store_result();
					
					if($stmt->num_rows > 0){
						
		                $stmt->bind_result($id, $name, $RoomNo, $description);
						$stmt->fetch();
						
						$room1 = array(
							'id'=>$id, 
							'name'=>$name,					
			                'RoomNo' =>$RoomNo, 
                            'Description' =>$description
						);
						$response['success'] = '1'; 
						$response['message'] = 'Data Get successfully'; 
						$response['room1'] = $room1; 
					}else{
						$response['error'] = true; 
						$response['message'] ='Some error occurred please try again';
					}
				}
			break;
			
			case 'roommem1':
				
				if(isTheseParametersAvailable(array('rmid'))){
					
					$id = $_POST['rmid'];
					$stmt = $connect->prepare("SELECT rmid,rmroomno,rmperson, rmdesignation, rmexperts from roommembers WHERE rmid = ?");
		
                                        $stmt->bind_param("i", $id );
							
					$stmt->execute();
					
					$stmt->store_result();
					
					if($stmt->num_rows > 0){
						
		                        $stmt->bind_result($id, $roomno, $person, $desig, $experts);
					$stmt->fetch();
						
					$roommem1 = array(
					'rmid'=>$id, 
			                'rmroomno' =>$roomno,
					'rmperson'=>$person,					
                                        'rmdesignation' =>$desig,
					'rmexperts'=>$experts			
						);
						$response['success'] = '1'; 
						$response['message'] = 'Data Get successfully'; 
						$response['roommem1'] = $roommem1; 
					}else{
						$response['error'] = true; 
						$response['message'] ='Some error occurred please try again';
					}
				}
			break;
			
			case 'officehours1':
				
				if(isTheseParametersAvailable(array('tmid'))){
					
					$id = $_POST['tmid'];
					$stmt = $connect->prepare("SELECT tmid, tmday, tmopen, tmclose,stfmemName from officetimings WHERE tmid = ?");
		
                    $stmt->bind_param("i", $id );
							
					$stmt->execute();
					
					$stmt->store_result();
					
					if($stmt->num_rows > 0){
						
		                $stmt->bind_result($id, $day, $open, $close , $stfmemName);
						$stmt->fetch();
						
						$time = array(
							'tmid'=>$id, 
							'tmday'=>$day,					
			                'tmopen' =>$open, 
					        'tmclose' =>$close, 
                            'stfmemName' =>$stfmemName
						);
						$response['success'] = '1'; 
						$response['message'] = 'Data Get successfully'; 
						$response['Timings'] = $time; 
					}else{
						$response['error'] = true; 
						$response['message'] ='Some error occurred please try again';
					}
				}
			break;
		}
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
 