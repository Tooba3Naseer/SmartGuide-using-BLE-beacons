<?php
 
class DbOperation
{
    //Database connection link
    private $connect;
 
    //Class constructor
    function __construct()
    {
	    require_once 'DbConnection.php';
        $db = new DbConnect();
        //Initializing our connection link of this class
        //by calling the method connect of DbConnect class
        $this->connect = $db->connect();
    }
	
	/*
	* The read operation
	* When this method is called it is returning all the existing record of the database
	*/
	function getRooms(){
		$stmt = $this->connect->prepare("SELECT id,Name,RoomNo, Description from roominfo");
		$stmt->execute();
		$stmt->bind_result($id, $name, $RoomNo, $Description);
		
		$rooms = array(); 
		
		while($stmt->fetch()){
			$room  = array();
			$room['id'] = $id; 
			$room['Name'] = $name; 
			$room['RoomNo'] = $RoomNo; 
            $room['Description'] = $Description; 
			array_push($rooms, $room); 
		}
		
		return $rooms; 
	}
	function getStaffMem(){
		$stmt = $this->connect->prepare("SELECT * from roommembers");
		$stmt->execute();
		$stmt->bind_result($id, $RoomNo,$name, $Designation, $Expert);
		
		$roomems = array(); 
		
		while($stmt->fetch()){
			$roommem  = array();
			$roommem['rmid'] = $id; 
			$roommem['rmroomno'] = $RoomNo; 
			$roommem['rmperson'] = $name; 
            $roommem['rmdesignation'] = $Designation; 
		    $roommem['rmexperts'] = $Expert; 

			array_push($roomems, $roommem); 
		}
		
		return $roomems; 
	}
	function getTimings(){
		$stmt = $this->connect->prepare("SELECT * from officetimings");
		$stmt->execute();
		$stmt->bind_result($id, $day,$tmopen, $tmclose, $roommems);
		
		$timings = array(); 
		
		while($stmt->fetch()){
			$timing  = array();
			$timing['tmid'] = $id; 
			$timing['tmday'] = $day; 
			$timing['tmopen'] = $tmopen; 
            $timing['tmclose'] = $tmclose; 
		    $timing['stfMemName'] = $roommems; 

			array_push($timings, $timing); 
		}
		
		return $timings; 
	}
	
	/*
	* The delete operation
	* When this method is called record is deleted for the given id 
	*/
	function deleteRoom($id){
		$stmt = $this->connect->prepare("DELETE FROM roominfo WHERE id = ? ");
		$stmt->bind_param("i", $id);
		if($stmt->execute())
			return true; 
		
		return false; 
	}
	function deleteRoomMemInfo($id){
		$stmt = $this->connect->prepare("DELETE FROM roommembers WHERE rmid = ? ");
		$stmt->bind_param("i", $id);
		if($stmt->execute())
			return true; 
		
		return false; 
	}
	function deleteTimings($id){
		$stmt = $this->connect->prepare("DELETE FROM officetimings WHERE tmid = ? ");
		$stmt->bind_param("i", $id);
		if($stmt->execute())
			return true; 
		
		return false; 
	}
	/*
	* The update operation
	* When this method is called the record with the given id is updated with the new given values
	*/
	function updateRoom($id, $name, $roomno, $description){
		$stmt =  $this->connect->prepare("UPDATE roominfo SET name = ?, RoomNo = ?, description = ? WHERE id = ?");
		$stmt->bind_param("sssi", $name, $roomno, $description, $id);
		if($stmt->execute())
			return true; 
		return false; 
	}
	function updateRoomMem($id, $RoomNo,$name, $Designation, $Expert){
		$stmt =  $this->connect->prepare("UPDATE roommembers SET rmroomno = ?,  rmperson= ?, rmdesignation = ? ,rmexperts = ? WHERE rmid = ?");
		$stmt->bind_param("ssssi", $RoomNo, $name, $Designation,  $Expert, $id);
		if($stmt->execute())
			return true; 
		return false; 
	}
	function updateTimings($id, $day,$tmopen, $tmclose, $roommems){
		$stmt =  $this->connect->prepare("UPDATE officetimings SET tmday = ?, tmopen = ?, tmclose = ?, stfMemName =? WHERE tmid = ?");
		$stmt->bind_param("ssssi", $day, $tmopen, $tmclose,$roommems, $id);
		if($stmt->execute())
			return true; 
		return false; 
	}
	
}