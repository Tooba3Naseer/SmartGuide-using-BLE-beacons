<?php 
 
	//getting the dboperation class
	require_once 'DbOperation.php';
 
	//function validating all the paramters are available
	//we will pass the required parameters to this function 
	function isTheseParametersAvailable($params){
		//assuming all parameters are available 
		$available = true; 
		$missingparams = ""; 
		
		foreach($params as $param){
			if(!isset($_POST[$param]) || strlen($_POST[$param])<=0){
				$available = false; 
				$missingparams = $missingparams . ", " . $param; 
			}
		}
		
		//if parameters are missing 
		if(!$available){
			$response = array(); 
			$response['error'] = true; 
			$response['message'] = 'Parameters ' . substr($missingparams, 1, strlen($missingparams)) . ' missing';
			
			//displaying error
			echo json_encode($response);
			
			//stopping further execution
			die();
		}
	}
	
	//an array to display response
	$response = array();
	
	//if it is an api call 
	//that means a get parameter named api call is set in the URL 
	//and with this parameter we are concluding that it is an api call
	if(isset($_GET['apicall'])){
		
		switch($_GET['apicall']){
			
		  
		
			//the READ operation
			//if the call is getRooms
			case 'getrooms':
				$db = new DbOperation();
				$response['roominfo'] = $db->getRooms();
                                $response["success"] = 1;
			break; 
			
			//the READ operation
			//if the call is getstaffmem
			case 'getstaffmem':
				$db = new DbOperation();
				$response['roommembers'] = $db->getStaffMem();
                                $response["success"] = 1;
			break; 
			//the READ operation
			//if the call is gettiming
			case 'gettiming':
				$db = new DbOperation();
				$response['officetimings'] = $db->getTimings();
                                $response["success"] = 1;
			break;
			//the delete room
			case 'deleteroom':
 
				//for the delete operation we are getting a GET parameter from the url having the id of the record to be deleted
				if(isset($_GET['id'])){
					$db = new DbOperation();
					if($db->deleteRoom($_GET['id'])){
						$response['error'] = false; 
						$response['message'] = 'Room deleted successfully';
						$response['roominfo'] = $db->getRooms();
					}else{
						$response['error'] = true; 
						$response['message'] = 'Some error occurred please try again';
					}
				}else{
					$response['error'] = true; 
					$response['message'] = 'Nothing to delete, provide an id please';
				}
			break; 
			//the delete roommembers
			case 'deleteroommembers':
 
				//for the delete operation we are getting a GET parameter from the url having the id of the record to be deleted
				if(isset($_GET['id'])){
					$db = new DbOperation();
					if($db->deleteRoomMemInfo($_GET['id'])){
						$response['error'] = false; 
						$response['message'] = 'RoomMemInfo deleted successfully';
						$response['roommembers'] = $db->getStaffMem();
					}else{
						$response['error'] = true; 
						$response['message'] = 'Some error occurred please try again';
					}
				}else{
					$response['error'] = true; 
					$response['message'] = 'Nothing to delete, provide an id please';
				}
			break; 
			//the delete timings
			case 'deletetimings':
 
				//for the delete operation we are getting a GET parameter from the url having the id of the record to be deleted
				if(isset($_GET['id'])){
					$db = new DbOperation();
					if($db->deleteTimings($_GET['id'])){
						$response['error'] = false; 
						$response['message'] = 'timings deleted successfully';
						$response['officetimings'] = $db->getTimings();
					}else{
						$response['error'] = true; 
						$response['message'] = 'Some error occurred please try again';
					}
				}else{
					$response['error'] = true; 
					$response['message'] = 'Nothing to delete, provide an id please';
				}
			break; 
			
			//the UPDATE operation
			case 'updateroom':
				isTheseParametersAvailable(array('id','Name','RoomNo','Description'));
				$db = new DbOperation();
				$result = $db->updateRoom(
					$_POST['id'],
					$_POST['Name'],
					$_POST['RoomNo'],
					$_POST['Description']
                   
	);
				
				if($result){
					$response['error'] = false; 
					$response['message'] = 'Room updated successfully';
					
				}else{
					$response['error'] = true; 
					$response['message'] = 'Some error occurred please try again';
				}
			break; 
			
			//the UPDATE operation
			case 'updateroommem':
				isTheseParametersAvailable(array('rmid', 'rmroomno',  'rmperson' , 'rmdesignation','rmexperts'));
				$db = new DbOperation();
				$result = $db->updateRoomMem(
					$_POST['rmid'],
					$_POST['rmroomno'],
					$_POST['rmperson'],
					$_POST['rmdesignation'],
				    $_POST['rmexperts']

					);
				
				if($result){
					$response['error'] = false; 
					$response['message'] = 'RoomMemInfo updated successfully';
					$response['roommembers'] = $db-> getStaffMem();
				}else{
					$response['error'] = true; 
					$response['message'] = 'Some error occurred please try again';
				}
			break; 
			
			//the UPDATE operation
			case 'updatetimings':
				isTheseParametersAvailable(array('tmid','tmday','tmopen','tmclose','stfmemName'));
				$db = new DbOperation();
				$result = $db->updateTimings(
					$_POST['tmid'],
					$_POST['tmday'],
					$_POST['tmopen'],
					$_POST['tmclose'],
					$_POST['stfmemName']);
				
				if($result){
					$response['error'] = false; 
					$response['message'] = 'Timings updated successfully';
				    $response['officetimings'] = $db->getTimings();
				}else{
					$response['error'] = true; 
					$response['message'] = 'Some error occurred please try again';
				}
			break; 
			
		}
		
	}else{
		//if it is not api call 
		//pushing appropriate values to response array 
		$response['error'] = true; 
		$response['message'] = 'Invalid API Call';
	}
	
	//displaying the response in json structure 
	echo json_encode($response);