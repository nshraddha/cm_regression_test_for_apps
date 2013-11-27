<?PHP
require_once("class.phpmailer.php");
require_once("formvalidator.php");
//echo "\n"."in fg_membersite";
class FGMembersite {
var $inactive= 900;
//var $inactive= 60;

 //var $location= 'C:/Leela/reg/';
 var $location= '/home/ec2-user/configTemp/';
 var $usersTempListFileLoc= "regesteredUsers.json";
 var $domainsTempListFileLoc= "unknownrgddomain.json";
 var $admin_email;
 var $from_address;
 
 
 
 
 

 var $username;
 var $pwd;
 var $database;
 var $tablename;
 var $connection;
 var $rand_key;

 var $error_message;
 var $text_message;

 var $domain_given;
 var $platformUserList;
 var $status_pending= "Confirmation Pending";
 
 /*
 $braces = "()";
 $symbols = "+-";
 $numbers = "0123456789";
 $allowed = $braces.$symbols.$numbers;
 */
 

 //-----Initialization -------
 function FGMembersite() {
  if(isset($_REQUEST['referer'])) {
   $referer= trim($_REQUEST['referer']);
  }
  elseif(isset($_SERVER['HTTP_REFERER'])) {
   $referer= base64_encode($_SERVER['HTTP_REFERER']);
  } else {
   $referer= "";
  }
  $this->sitename= 'www.cloudmunch.com';
  $this->rand_key= '0iQx5oBk66oVZep';
  $this->from_address= "info@cloudmunch.com";
  
  $host= explode(".", $_SERVER['HTTP_HOST']);
   $host= $host[0];

   $this->platformUserList= '/var/cloudbox/'.$host.'/data/userlist.json';
  
 }

 function InitDB($host, $uname, $pwd, $database, $tablename) {
  $this->db_host= $host;
  $this->username= $uname;
  $this->pwd= $pwd;
  $this->database= $database;
  $this->tablename= $tablename;

 }
 function SetAdminEmail($email) {
  $this->admin_email= $email;
 }

 function SetWebsiteName($sitename) {
  $this->sitename= $sitename;
 }

 function SetRandomKey($key) {
  $this->rand_key= $key;
 }

 //-------Main Operations ----------------------
 function Register() {
  if(!isset($_POST['submitted'])) {
	//echo "\nform not submitted\n";
   return false;
  }

  $formvars= array();

  if(!$this->ValidateRegistrationSubmission()) {
	//echo "\nregistration validation failed\n";
   return false;
  }

//  if(!(is_numeric($_POST['number']))){
//  	if(
//  	( (preg_match('/[-+ 0-9]/', $_POST['number']) &&  preg_quote($_POST['number'], '()') ) && preg_match('/[a-zA-Z`~!@#$%^&*/\:";]/', $_POST['number']) )  
//  	||
//  	( (preg_match('/[-+ 0-9]/', $_POST['number']) &&  preg_quote($_POST['number'], '()') ) && preg_quote($_POST['number'], '{}[]=_\/|<>.,') )
//  	||
//  	( preg_match('/[a-zA-Z`~!@#$%^&*:";]/', $_POST['number']) ||  preg_quote($_POST['number'], '{}[]=_\/|<>.,') )){
//  		$this->HandleError("Enter a valid telephone number");
//  		error_log("invalid telephone number");
//  		return false;	
//  	}
//  	else{
//    	error_log("valid telephone number");
//  	}
//  }
if(!(is_numeric($_POST['number']))){
if (preg_match("/[^\d\s \(\)+-]/", $_POST['number'])) {
    //Invalid
    $this->HandleError("Enter a valid telephone number");
	error_log("invalid telephone number");
	return false;	
    
}
}

  require_once('recaptchalib.php');
   $privatekey= "6LcGaM8SAAAAABIUASkxl146vX3HPuYdCuO41owQ";
  $resp= recaptcha_check_answer($privatekey, $_SERVER["REMOTE_ADDR"], $_POST["recaptcha_challenge_field"], $_POST["recaptcha_response_field"]);

  if(!$resp->is_valid) {
   // What happens when the CAPTCHA was entered incorrectly
   $this->HandleError("The code entered in CAPTCHA is not correct. Please try again.");
   return false;
  } else {
 
   // Your code here to handle a successful verification
	//echo "\nformvar - ".$formvars;
   $this->CollectRegistrationSubmission($formvars);
   
   //$ip = $formvars['ip'];
   //error_log("ip".$ip);
   
   $this->domain_given= explode("@", $formvars['email']);
   $this->domain_given= $this->domain_given[(count($this->domain_given) - 1)];

   $this->domain_no_extn= explode(".", $this->domain_given);
   $this->domain_no_extn= $this->domain_no_extn[0];
   error_log();
   error_log("purpose - ".$formvars['purpose']);

   if($this->FromSite()) {
    $found= false;
	//echo "\ndomain.json - ".$this->location;
    if(file_exists($this->location."domain.json")) {
	//echo "\ndomain.json exists\n";
     $string= file_get_contents($this->location."domain.json");
     $jsonContent= json_decode($string);

     foreach($jsonContent->Domains as $Domain) {
      if(strtoupper($Domain->DomainName) == strtoupper($this->domain_no_extn)) {
       $found= true;
       break;
      }

     }
    } else {
     $found= true;
    }
    if(!$found) {
    
    		$confirmcode= $this->MakeConfirmationMd5($formvars['email']);
     		$formvars['confirmcode']= $confirmcode;

     		$this->registerUserToJSON($formvars);
     		$this->SendUserConfirmationEmail($formvars);
     		return true; 	
    } 
    else {
     $domainsArray= $this->readUsersListFile($this->location.$this->domainsTempListFileLoc);
     $this->domain_given= explode("@", $formvars['email']);
     $this->domain_given= $this->domain_given[(count($this->domain_given) - 1)];
     $domain= $this->domain_given;
     date_default_timezone_set('UTC');
     $domainsArray-> $formvars['email']->name= $formvars['name'];
     $domainsArray-> $formvars['email']->email= $formvars['email'];
     $this->writeToTempDomainsListFile($domainsArray);
     $this->SendIneligibleUserEmail($formvars);
     $this->SendAdminIntimationEmailAboutUnKnown($formvars);
     //$this->RedirectToURL("index.php?option=com_content&view=article&id=131");
     $this->RedirectToURL("/index.php/thanks-for-interest");

    }
   } else {
    $host= $_SERVER['HTTP_HOST'];
    $this->domain_no_extn= explode(".", $this->domain_given);
    $this->domain_no_extn= $this->domain_no_extn[0];

    if(substr_compare($host, $this->domain_no_extn, 0,strlen($this->domain_no_extn), TRUE) == 0) {
     $confirmcode= $this->MakeConfirmationMd5($formvars['email']);
     $formvars['confirmcode']= $confirmcode;

     $this->registerUserToJSON($formvars);
     $this->SendUserConfirmationEmail($formvars);
     return true;
    } else {
     $this->RedirectToURL("thank-you-nonplatform-user.php");
    }
   }
  }
 }

 function checkUserExists($userMailID, $existingUsersList) {
  foreach($existingUsersList as $userInfo) {
   if($userInfo->email == $userMailID) {
    return true;
   }
  }
  return false;

 }

	function ContactUs() {
	
		if(!isset($_POST['submitted'])) {
			error_log("form not submitted");
   			return false;
  		}
  		 $formvars= array();
  		
  		if(!$this->ValidateContactUsSubmission()) {
			error_log("validation error");
   			return false;
  		}
  		$this->CollectContactUsSubmission($formvars);
		$this->SendAdminIntimationEmailContactUs($formvars);
		error_log("sent mail to admin");
		return true;
	}



	function ValidateContactUsSubmission() {
 		 //This is a hidden input field. Humans won't fill this field.
  		if(!empty($_POST[$this->GetSpamTrapInputName()])) {
   			//The proper error is not given intentionally
   			$this->HandleError("Automated submission prevention: case 2 failed");
   			return false;
  		}

  		$validator= new FormValidator();
  		$validator->addValidation("name", "req", "Please fill in your Name.");
  		$validator->addValidation("email", "email", "The input for Email should be a valid email value.");
  		$validator->addValidation("email", "req", "Please fill in your Corporate Email.");
  		$validator->addValidation("subject", "req", "Please enter subject.");
  		$validator->addValidation("message", "req", "Please enter a message.");
		  
  		if(!$validator->ValidateForm()) {
   			$error= '';
   			$error_hash= $validator->GetErrors();
   			foreach($error_hash as $inpname => $inp_err) {
    			$error .= $inp_err."\n";
   			}
   			$this->HandleError($error);
   			return false;
  		}
  		
  		return true;
 	}
 	
 	
 	function CollectContactUsSubmission(& $formvars) {
  		
  		//echo "val of name - ".$_POST['name'];
  		$formvars['name']= $this->Sanitize($_POST['name']);
  		$formvars['email']= $this->Sanitize($_POST['email']);
  		$formvars['subject']= $this->Sanitize($_POST['subject']);
  		$formvars['ip']= $this->Sanitize($_POST['ip']);
  		$formvars['message']= $this->Sanitize($_POST['message']);
  		 
 }
 	
 	
 	function SendAdminIntimationEmailContactUs(& $formvars) {
  		if(empty($this->admin_email)) {
   			return false;
  		}
  		$mailer= new PHPMailer();
		
  		$mailer->CharSet= 'utf-8';

  		$mailer->AddAddress($this->admin_email);

  		$mailer->Subject= "Contact CloudMunch for ".$formvars['subject'];

	  $mailer->From= $this->GetFromAddress();

	  $mailer->Body= "Customer feedback/query from  ".$this->sitename."\r\n".
  		"Name: ".$formvars['name']."\r\n".
  		"Email address: ".$formvars['email']."\r\n".
  		"Message: ".nl2br($formvars['message'])."\r\n".
  		"IP Address: ".$formvars['ip'];

	  if(!$mailer->Send()) {
   		return false;
  	  }
  	  return true;
 }
 	

 function writeToTempDomainsListFile($usersDetails) {
  $info= json_encode($usersDetails);
  $file= fopen($this->location.$this->domainsTempListFileLoc, 'w+');
  fwrite($file, $info);
  fclose($file);
 }

 function readUnknownDomainsListFile() {

  if(!file_exists($this->location.$this->domainsTempListFileLoc)) {
   $file= fopen($this->location.$this->domainsTempListFileLoc, 'w+');
   fwrite($file, "");
  }
  $file= fopen($this->location.$this->domainsTempListFileLoc, 'r+');

  $details= fread($file, filesize($this->location.$this->domainsTempListFileLoc));
  $detailArray= json_decode($details);
  fclose($file);
  return $detailArray;
 }

 function updateEntry() {
  if(!isset($_POST['submitted'])) {
   return false;
  }
  if(!isset($_SESSION)) {
   session_start();
  }

  $formvars= array();
  $this->CollectRegistrationOnConfirm($formvars);

  $found= false;
  $confirmcode= $_SESSION['code'];
  $string= file_get_contents($_SESSION['file']);
  $jsonContent= json_decode($string);

  foreach($jsonContent as $user) {
   if($user->confirmcode == $confirmcode) {
    $found= true;
    $user->status= "confirmed";
    $user_rec['name']= $user->first;
    $user_rec['email']= $user->email;
    $this->writeToUsersListFile($jsonContent);
    break;
   }
  }

  if(!$found) {
   $this->HandleError("Wrong confirm code.");
   return false;
  }
  return true;
 }

 function updateEC2DetailsInJSON($emailID, $instanceurl, $trailStartedAt, $filePath) {
  $string= file_get_contents($filePath);
  $jsonContent= json_decode($string);

  foreach($jsonContent as $user) {
   if($user->email == $emailID) {
    $found= true;
    $user_rec['name']= $user->first;
    $user_rec['email']= $user->email;

    $this->domain_given= explode("@", $user->email);
    $this->domain_given= $this->domain_given[(count($this->domain_given) - 1)];

    $this->domain_no_extn= explode(".", $this->domain_given);
    $this->domain_no_extn= $this->domain_no_extn[0];

    $fileName= $this->location.$user->email.'_usersList.json';

    $user->instanceurl= $instanceurl;
    $user->trailStartedAt= $trailStartedAt;

    $this->writeToUsersListFile($jsonContent);
    $file= fopen($fileName, 'w+');
    fwrite($file, $info= json_encode($user));
    fclose($file);
    break;
   }
  }
 }
 function registerUserToJSON(& $formvars) {
  $fileName= $this->location.$this->usersTempListFileLoc;
  if(!$this->FromSite()) {
   $fileName= $this->platformUserList;
   //   $fileName= "C:/Leela/reg/userlist.json";
  }
  $usersArray= $this->readUsersListFile($fileName);
  if($this->checkUserExists($formvars['email'], $usersArray)) {
   //$this->RedirectToURL("index.php?option=com_content&view=article&id=134");
   $this->RedirectToURL("/index.php/user-exists-already");

  } else {
   date_default_timezone_set('UTC');
   $usersArray-> $formvars['email']->first= $formvars['name'];
   $usersArray-> $formvars['email']->last= $formvars['lastname'];
   $usersArray-> $formvars['email']->email= $formvars['email'];
   $usersArray-> $formvars['email']->number= $formvars['number'];
   $usersArray-> $formvars['email']->purpose= $formvars['purpose'];
   $usersArray-> $formvars['email']->status= $this->status_pending;
   $usersArray-> $formvars['email']->confirmcode= $formvars['confirmcode'];
   $usersArray-> $formvars['email']->createddate= date("Y-m-d-H:i:s");
   $usersArray-> $formvars['email']->role= "developer";

   $this->writeToUsersListFile($usersArray, $fileName);
  }
 }

 function writeToUsersListFile($usersDetails, $fileName) {
  $info= json_encode($usersDetails);
  $file= fopen($fileName, 'w+');
  fwrite($file, $info);
  fclose($file);
 }

 function readUsersListFile($fileName) {
  //                                  $fileName= $this->location.$this->usersTempListFileLoc;
  if(!file_exists($fileName)) {
   $file= fopen($fileName, 'w+');
   fwrite($file, "");
  }
  $file= fopen($fileName, 'r+');

  $details= fread($file, filesize($fileName));
  $detailArray= json_decode($details);
  fclose($file);
  return $detailArray;
 }

 function RegisterUser() {
  if(!isset($_POST['submitted'])) {
   return false;
  }

  $formvars= array();

  if(!$this->ValidateRegistrationSubmission()) {
   return false;
  }

  $this->CollectRegistrationSubmission($formvars);

  if(!$this->SaveToDatabase($formvars)) {
   return false;
  }
  error_log("before sending user confirmation mail");
  if(!$this->SendUserConfirmationEmail($formvars)) {
   return false;
	error_log("send user confirmation mail failed");
  }
  error_log("before sending admin intimation mail");
  $this->SendAdminIntimationEmail($formvars);
  error_log("after sending admin intimation mail");
  return true;
 }

 function isConfirmedAlready() {
	//echo "\nreq code, is confirmed already- ".$_REQUEST['code'];
  if(empty($_REQUEST['code']) || strlen($_REQUEST['code']) <= 10) {
	//echo "\nprovide confirm code";
   $this->HandleError("Please provide the confirm code");
   return false;
  }
  $found= false;
  $user_rec= array();
  $confirmcode= $_REQUEST['code'];
  $fileName= $this->location.$this->usersTempListFileLoc;
  if(!$this->FromSite()) {
   $fileName= $this->platformUserList;
   //   $fileName= "C:/Leela/reg/userList.json";
  }
  $usersArray= $this->readUsersListFile($fileName);
	//echo "\nfilename - ".$fileName;
  $string= file_get_contents($fileName);
	//echo "\nfilecontents - ".$string;
  $jsonContent= json_decode($string);
/*
  if (!empty($_POST)){
	//echo "\ncheck form post";
	//echo "\nnewemail - ".$_REQUEST['nemail'];
       $user= $jsonContent->$_REQUEST['nemail'];
   }
*/
  
	//echo "\n no post";
	//echo "\email - ".$_REQUEST['email'];
       $user= $jsonContent->$_REQUEST['email'];
  
	//echo "\n1user - ".$user;
  if(!isset($user->confirmcode) && $user->status == "Active") {
	//echo "\nreg is already confirmed";
   $this->HandleError("Your registration is already confirmed.");
   return true;
  }
      	//echo "\nuser code - ".$user->confirmcode;
	//echo "\nconfirm code - ".$confirmcode;
   if($user->confirmcode == $confirmcode) {
    $found= true;
    if($user->status == "Active") {
     $this->HandleError("Your mail id is already confirmed.");
     return true;
    } else {
     return false;
   }
  }
  if(!$found) {
	//echo "\nfound false";
   $this->HandleError("Wrong confirm code.");
 // $this->RedirectToURL("index.php?option=com_content&view=article&id=133");
   return false;
  }
 }

 function ConfirmUser() {
	//echo "\n req code, confirm user - ".$_REQUEST['code'];
  if(empty($_REQUEST['code']) || strlen($_REQUEST['code']) <= 10) {
   $this->HandleError("Please provide the confirm code");
   return false;
  }

  if(empty($_REQUEST['password']) || strlen($_REQUEST['password']) <= 8) {
   $this->HandleError("Please provide the password and it should have more than eight characters!");
   return false;
  }
  if(empty($_REQUEST['confpassword'])) {
   $this->HandleError("You have not re-entered your password");
   return false;
  }

  if(($_REQUEST['confpassword']) !=($_REQUEST['password'])) {
   $this->HandleError("Passwords are not matching. Please enter same password in both places");
   return false;
  }

  $found= false;
  $user_rec= array();
  $confirmcode= $_REQUEST['code'];
  $file= $this->location.$this->usersTempListFileLoc;
	//echo "\nfile - ".$file;
  if(!$this->FromSite()) {
   $file= $this->platformUserList;
   //   $file= "C:/Leela/reg/userlist.json";
  }


  $string= file_get_contents($file);
  $jsonContent= json_decode($string);

  $user= $jsonContent->$_REQUEST['email'];

  if(isset($user->confirmcode) && $user->confirmcode == $confirmcode) {
	//echo "\ncheck user status";
   if($user->status == "Active") {
	//echo "\nstatus active";
    $this->HandleError("Your mail id is already confirmed.");
    //$this->RedirectToURL("index.php?option=com_content&view=article&id=135");
    $this->RedirectToURL("/index.php/registration-confirmed-already");

    return false;
   }
   $found= true;
   $user_rec['name']= $user->first;
   $user_rec['email']= $user->email;
   $user_rec['purpose']= $user->purpose;
   $user_rec['number']=$user->number;
   $user_rec['ip_user']=$_REQUEST['ip_user'];
   error_log("user confirmation from post ".$_REQUEST['ip_user']);
   error_log("user confirmation from user record ".$user_rec['ip_user']);

   $this->domain_given= explode("@", $user->email);
   $this->domain_given= $this->domain_given[(count($this->domain_given) - 1)];

   $this->domain_no_extn= explode(".", $this->domain_given);
   $this->domain_no_extn= $this->domain_no_extn[0];

   $fileName= $this->location.$user->email.'_usersList.json';

   $user->status= "Active";
   $user->trialRequested= "false";
   $user->pass= md5($_REQUEST['password']);
   $user->domain= $this->domain_no_extn;
   $user->userListFile= $fileName;
   unset($user->confirmcode);
   $this->writeToUsersListFile($jsonContent, $file);
   $file= fopen($fileName, 'w+');

   $userEntry= $this->readUsersListFile($fileName);
   $userEntry-> $_REQUEST['email']= $user;
  //   $userEntry-> $_REQUEST['nemail']= $user;
   $this->writeToUsersListFile($userEntry, $fileName);
   
   // For Community 
//   if(chdir ('/var/www/html/cloudbox/work/community/')){
//   	$command = 'java -Xmx512m -cp selenium-server-standalone-2.16.1.jar:. cbCommunitySignup '.$user->first.' '.$user->email.' '.$_REQUEST['password'].' >>signup.txt 2>&1';
//	exec($command);    
//   }
   
   
  }

  if(!$found) {
   $this->HandleError("Wrong confirm code.");
  // $this->RedirectToURL("index.php?option=com_content&view=article&id=133");
   return false;
  }
  if( $user_rec['purpose'] == "personal"){
  	$this->SendUserWelcomeEmailPersonal($user_rec);
  }
  else{
    $this->SendUserWelcomeEmail($user_rec);
  }
  $this->SendAdminIntimationOnRegComplete($user_rec);

  return true;
 }

 function ConfirmUser_OLD() {
  if(empty($_GET['code']) || strlen($_GET['code']) <= 10) {
   $this->HandleError("Please provide the confirm code");
   return false;
  }
  if(!isset($_SESSION)) {
   session_start();
  }
  $user_rec= array();
  if(!$this->checkConfirmCodeExists($user_rec, $_GET['code'], $_GET['placeholder'])) {
   $this->RedirectToURL("Sorry.html");
  } else {
   //															$_SESSION['confimcode'] = $_GET['code'];
   //															$_SESSION['file'] = $_GET['placeholder'];
   //															$this->RedirectToURL("finalConfirmation.php");
  }

  $this->SendUserWelcomeEmail($user_rec);

  $this->SendAdminIntimationOnRegComplete($user_rec);

  return true;
 }

 function checkConfirmCodeExists(& $user_rec, & $getconfirmcode, & $file) {
  $found= false;
  $confirmcode= $getconfirmcode;
  $string= file_get_contents($file);
  $jsonContent= json_decode($string);

  foreach($jsonContent as $user) {
   if($user->confirmcode == $confirmcode) {
    $found= true;
    $user_rec['name']= $user->first;
    $user_rec['email']= $user->email;
    break;
   }
  }

  if(!$found) {
   $this->HandleError("Wrong confirm code.");
   return false;
  }
  return true;
 }

 function CheckLoginDetailsInUsersList($username, $password) {
  $username= $this->Sanitize($username);
  $pwdmd5= md5($password);
  $match= false;
  $userexists= false;

  $fileName= $this->location.$this->usersTempListFileLoc;
  if(!$this->FromSite()) {
   $fileName= $this->platformUserList;
   //   $fileName= "C:/Leela/reg/userList.json";
  }
  $usersArray= $this->readUsersListFile($fileName);

  foreach($usersArray as $userInfo) {
   if($userInfo->email == $username) {
    if($userInfo->status != 'Active') {
     $this->HandleError("Your registration is not active. You might have not confirmed your registration");
     return false;
    }

    $userexists= true;
    if($userInfo->pass == md5($password)) {
     $match= true;
     $_SESSION['name_of_user']= $userInfo->first;
     $_SESSION['domain_of_user']= $userInfo->domain;
     $_SESSION['userList_Loc']= $userInfo->userListFile;
     $_SESSION['email_of_user']= $userInfo->email;
     $_SESSION['pw_of_user']= $password;
	error_log("setting session");
    }
   }
  }
  if($match) {
   return true;
  } else {
   if($userexists) {
	//echo "\nuserexists error\n";
    $this->HandleError("Error logging in. The username or password does not match");
    return false;
   } else {
    $this->HandleError("Error logging in. User not found");
    return false;
   }
  }

 }

 function FromSite() {
  // 	$filelist = file_Get_contents("/var/cloudbox/data/cloudboxconfig.json");
  $configFile= "/var/cloudbox/data/cloudboxconfig.json";
	//echo "\nconfigfile-".$configFile;
  //  $configFile ="C:/Leela/reg/cloudboxconfig.json";
  if(file_exists($configFile)) {
//	echo "\nconfigfile exists";
   $filelist= file_Get_contents($configFile);
//	echo "\nfilelist-".$filelist;
   $json= json_decode($filelist);
	//echo "json-".$json;
   if(isset($json->host)) {
//	echo "\njson->host is set - ".$json->host;
    if(($_SERVER['HTTP_HOST'] == $json->host) || ($_SERVER['HTTP_HOST'] == preg_replace('/^www\./','',$json->host))) {
//	echo " \ntrue1 ";
     return true;
    } else {
//	echo " false 1 ";
     return false;
    }
   } else {
//	echo " false2 ";
    return false;
   }
  } else {
//	echo " false3 ";
   return false;
  }

 }

  function GetUserDomainURL(){
	  
	  $email_user = $_SESSION['email_of_user'];
	  error_log("FGemail_user - ".$email_user);
  	  $email_id = explode("@", $email_user);
  	  error_log("FGemail_id - ".$email_id);
	  error_log("FGemail_id[1] - ".$email_id[1]);
	  $email_domain = explode(".",$email_id[1]);
	  error_log("FGemail_domain - ".$email_domain);
	  $user_domain_url = "http://".$email_domain[0].".cloudmunch.com/cmdashboard/login.html";
	  error_log("FGuser_domain_url - ".$user_domain_url);
	  return $user_domain_url;
  }

  function GetServerResponse($site_url){
	
	$user_domain_url = $site_url;
	error_log("FGuser_domain_url - ".$user_domain_url);
	$url = parse_url($user_domain_url);
	$host = $url['host'];
	error_log("FGhost - ".$host);
	$port = $url['port'];
	error_log("FGport - ".$port);
	$path = $url['path'];
	error_log("FGpath - ".$path);
	$query = $url['query'];
	error_log("FGquery - ".$query);
	if(!$port)
		$port = 80;
	$request = "HEAD $path?$query HTTP/1.1\r\n"
                  ."Host: $host\r\n"
          	    ."Connection: close\r\n"
                  ."\r\n";
	$address = gethostbyname($host);
	$socket = socket_create(AF_INET, SOCK_STREAM, SOL_TCP);
	socket_connect($socket, $address, $port);
	socket_write($socket, $request, strlen($request));
	$response = split(' ', socket_read($socket, 1024));
       error_log("response - ".$response[1]);
	
	if($response[1] == "200"){
 		error_log("response:200");
   		//echo $user_domain_url;

   		error_log("FGuser_domain_url[2]".$user_domain_url);
   		error_log("FGuser_domain".$user_domain);
		return $user_domain_url;
	}
	else{
		socket_close($socket);
	     //echo "/index.php/instance-not-available";
		return "/index.php/instance-not-available";
	}
	socket_close($socket);
  }


  function GetDomainURL(){
	
	  $email_user = $_SESSION['email_of_user'];
	  error_log("Iemail_user - ".$email_user);
	  $email_id = explode("@", $email_user);
	  error_log("Iemail_id - ".$email_id);
	  error_log("Iemail_id[1] - ".$email_id[1]);
	  $email_domain = explode(".",$email_id[1]);
	  error_log("Iemail_domain - ".$email_domain);
	  $user_domain_url = "http://".$email_domain[0].".cloudmunch.com";
	  error_log("Iuser_domain_url - ".$user_domain_url);
	  $user_instance_url = $email_domain[0].".cloudmunch.com";
	  return $user_instance_url;

  }


 function Login() {
 if(empty($_POST['email'])) {
   $this->HandleError("User ID is empty!");
   return false;
  }
  if(empty($_POST['password'])) {
   $this->HandleError("Password is empty!");
   return false;
  }
  $fileName= $this->location.$this->usersTempListFileLoc;
//	echo "got filename-".$fileName."\n";
	
  if(!$this->FromSite()) {
//	echo "\n"."inside !$this->FromSite";
   $fileName= $this->platformUserList;
//	echo 'platformUserList-'.$fileName.'\n';
   //   $fileName= "C:/Leela/reg/userList.json";
  }
//	echo "\n true or false - ".$this->FromSite();
//	echo "\n".getcwd();
//	exec('sudo cd /home/ec2-user');
//	chdir('/home/ec2-user');
//	echo "\n".getcwd();
//	echo "\n filename again - ".$fileName;
	if(is_file($fileName)){
//		echo "\nexists";
	}
	else{
//		echo "\ndoesnot exist";
	}
  if(!file_exists('/home/ec2-user/configTemp/regesteredUsers.json')) {
//	echo "\nfile doesnot exist";
	print_r(error_get_last());
   $this->HandleError("Error logging in. The username or password does not match");
   return false;
  }
  $usersArray= $this->readUsersListFile($fileName);
//  echo "file exists - ".$fileName;
   
  $found= false;

  $usersArray= $this->readUsersListFile($fileName);
//  echo "usersArray - ".$usersArray;
  foreach($usersArray as $userInfo) {
   if($userInfo->email == $_POST['email']) {
//	echo "email matches";
    $found= true;
    if($userInfo->status != 'Active') {
     $this->HandleError("Your registration is not active. You might have not confirmed your registration");
     return false;
    }
//	echo "reg is active";
   }
  }
  if(!$found) {
//	echo "email doesnot match";
   $this->HandleError("Error logging in. The username or password does not match");
   return false;
  }
 
  $email= trim($_POST['email']);
  $password= trim($_POST['password']);

  if(!isset($_SESSION)) {
   session_start();
  }

  if(!$this->CheckLoginDetailsInUsersList($email, $password)) {
   return false;
  }

  $_SESSION[$this->GetLoginSessionVar()]= $email;
  echo "session var - ".$this->GetLoginSessionVar();
	echo "email in session - ".$email;
  $_SESSION['start']= time();
  $_SESSION['loggedin']= 1; // Sets the session 'loggedin' to 1 

 // For Community 
//   if(chdir ('/var/www/html/cloudbox/work/community/')){
//   	$command = 'java -Xmx512m -cp selenium-server-standalone-2.16.1.jar:. cbCommunityLogin '.$_SESSION['name_of_user'].' '.$_POST['email'].' '.$_POST['password'].' >>login.txt 2>&1';
//	exec($command);    
//   }
//   
  return true;
 }

 function CheckLogin() {
	error_log("function CheckLogin");
  if(!isset($_SESSION)) {
   session_start();
   error_log("function CheckLogin-session not set");
  }

  $sessionvar= $this->GetLoginSessionVar();

  if(empty($_SESSION[$sessionvar])) {
   error_log("function CheckLogin-session vars empty");
   return false;
  }
  if(isset($_SESSION['start'])) {
   $session_life= time() - $_SESSION['start'];
   if($session_life > $this->inactive) {
    session_destroy();
    
    $this->HandleError("Your session is expired. Please login again!");
    //$this->RedirectToURL("/index.php");
    return false;
   }
  }
  error_log("function CheckLogin-session set");
  return true;
 }

 function UserFullName() {
  return isset($_SESSION['name_of_user']) ? $_SESSION['name_of_user'] : '';
 }

 function UserEmail() {
  return isset($_SESSION['email_of_user']) ? $_SESSION['email_of_user'] : '';
 }
 function UserPassword($mailID) {
    $fileName= $this->location.$this->usersTempListFileLoc;
  if(!$this->FromSite()) {
   $fileName= $this->platformUserList;
  }
  $usersArray= $this->readUsersListFile($fileName);

$userInfo = $usersArray->$mailID;
  return $userInfo->pass;
 }

 function LogOut() {
 	
 	// create a new cURL resource
$ch = curl_init();

// set URL and other appropriate options
curl_setopt($ch, CURLOPT_URL, "http://community.cloudmunch.com/account/signout/?next=/account/logout/");
curl_setopt($ch, CURLOPT_HEADER, 0);

// grab URL and pass it to the browser
curl_exec($ch);

// close cURL resource, and free up system resources
curl_close($ch);
 	
 	
  session_start();

  $sessionvar= $this->GetLoginSessionVar();

  $_SESSION[$sessionvar]= NULL;

  unset($_SESSION[$sessionvar]);
  session_destroy();
  return true;
 }

 function EmailResetPasswordLink(& $emailEntered) {if(empty($emailEntered)) {
   $this->HandleError("Email is empty!");
   return false;
  }
  $user_rec= array();
  if(false === $this->GetUserFromEmailInJSON($emailEntered, $user_rec)) {
   return false;
  }
  if(false === $this->SendResetPasswordLink($user_rec)) {
   return false;
  }
  return true;

 }

 function ResetPassword()
	{
		if(empty($_POST['newpwd']) || strlen($_REQUEST['newpwd']) <= 8)
		{
			$this->HandleError("New password is empty and it should have more than eight characters!");
			return false;
		}
		if(empty($_REQUEST['confPass']))
		{
			$this->HandleError("Please re-enter same password");
			return false;
		}
		if(($_REQUEST['confPass']) !=($_REQUEST['newpwd']))
		{
			$this->HandleError("Passwords are not matching. Please enter same password while confirming");
			return false;
		}
		if(empty($_POST['email']))
		{
			$this->HandleError("Email is empty!");
			return false;
		}
		if(empty($_POST['code']))
		{
			$this->HandleError("reset code is empty!");
			return false;
		}
		$email= trim($_POST['email']);
		$code= trim($_POST['code']);
		$file= $this->location.$this->usersTempListFileLoc;
		if(!$this->FromSite())
		{
			$file= $this->platformUserList;
			//   $file= "C:/Leela/reg/userlist.json";
		}
		$string= file_get_contents($file);
		$jsonContent= json_decode($string);
		$user= $jsonContent-> $_REQUEST['email'];
		if($user->resetCode == $code)
		{
			if($user->resetreqStatus == "Active")
			{
				$days=(strtotime(date("Y-m-d-H:i:s")) - strtotime($user->resetreqDate)) /(60 * 60 * 24);
				$from= date('d-m-Y', strtotime(date("Y-m-d")));
				$to= date('d-m-Y', strtotime($user->resetreqDate));
				$cnt= 0;
				$nodays=(strtotime($to) - strtotime($from)) /(60 * 60 * 24); //it will count no. of days
				if($days >= 1)
				{
					$user->resetreqStatus == "Expired";
					$this->HandleError("Reset code is expired!");
					$this->writeToUsersListFile($jsonContent, $file);
					return false;
				} else
				{
					$user_rec= array();
					if(!$this->GetUserFromEmailInJSON($email, $user_rec))
					{
						return false;
					}
					$new_password= trim($_POST['newpwd']);
					if(false === $new_password || empty($new_password))
					{
						$this->HandleError("Error updating new password");
						return false;
					}
					if(!$this->ChangePasswordInUserEntry($user_rec, $new_password))
					{
						$this->HandleError("Error updating new password");
						return false;
					}
				}
			} else
			{
				$this->HandleError("Reset code is expired!");
				return false;
			}
		} else
		{
			$this->HandleError("Bad reset code!");
			return false;
		}
		$this->LogOut();
		return true;
	}

 function CheckInstanceAvailable() {
  $user_rec= array();
  if(!$this->GetUserFromEmailInJSON($this->UserEmail(), $user_rec)) {
   return false;
  }
  $file= $this->location.$this->usersTempListFileLoc;
  if(!$this->FromSite()) {
   $file= $this->platformUserList;
  }
  $string= file_get_contents($file);
  $jsonContent= json_decode($string);

  foreach($jsonContent as $user) {
   if($user->email == $user_rec['email']) {
    $found= true;
    if(isset($user->instanceurl)) {
     return true;
    } else {
     return false;
    }
   }
  }

 }
 function CheckInstanceRequested() {
  $user_rec= array();
  if(!$this->GetUserFromEmailInJSON($this->UserEmail(), $user_rec)) {
    return false;
  }
  $file= $this->location.$this->usersTempListFileLoc;
  if(!$this->FromSite()) {
   $file= $this->platformUserList;
  }
  $string= file_get_contents($file);
  $jsonContent= json_decode($string);
  $user = $jsonContent->$_SESSION['email_of_user'];
  error_log("session in fg_ ".$_SESSION['email_of_user']);
   if($user->trialRequested == "true"){
   //echo "\n trial req ";
   return true;
  }else{
   // echo "\n not req ";
   return false;
  }

 }

 function getProjectInfo() {
  if(!$this->CheckLogin()) {
   $this->HandleError("Not logged in!");
   return false;
  }
  $email= $_SESSION['email_of_user'];

  $fileName= $this->location.$this->usersTempListFileLoc;
  if(!$this->FromSite()) {
   $fileName= $this->platformUserList;
   //   $fileName= "C:/Leela/reg/userList.json";
  }
  $jsonContent= $this->readUsersListFile($fileName);
  $user= $jsonContent-> $email;
  if($user->email == $email) {
   $user->Organisation->OtherTechonology= $_POST['orgOther'];
   if(isset($_POST['orgOnCloud'])) {
    $user->Organisation->isOnCloud= $_POST['orgOnCloud'];
   }
   $user->Organisation->strength= $_POST['orgCount'];
   $user->Organisation->challenges= $_POST['orgChallenges'];
   if(isset($user->Organisation->Techonologies)) {
    $val= $user->Organisation->Techonologies;
   } else {
    $val= "";
   }
   if(isset($_POST['orgJava'])) {
    $user->Organisation->Techonologies= $val.",".$_POST['orgJava'];
    $user->Organisation->Techonologies= $_POST['orgJava'];
   }
   if(isset($_POST['orgPHP'])) {
    $user->Organisation->Techonologies= $val.",".$_POST['orgPHP'];
    $user->Organisation->Techonologies= $_POST['orgPHP'];
   }
   if(isset($_POST['orgNet'])) {
    $user->Organisation->Techonologies= $val.",".$_POST['orgNet'];
    $user->Organisation->Techonologies= $_POST['orgNet'];
   }
   if(isset($_POST['orgRuby'])) {
    $user->Organisation->Techonologies= $val.",".$_POST['orgRuby'];
    $user->Organisation->Techonologies= $_POST['orgRuby'];
   }

   $user->Project->OtherTechonology= $_POST['projOther'];
   if(isset($_POST['projOnCloud'])) {
    $user->Project->isOnCloud= $_POST['projOnCloud'];
   }
   $user->Project->strength= $_POST['projCount'];
   $user->Project->challenges= $_POST['projChallenges'];
   if(isset($user->Project->Techonologies)) {
    $val= $user->Project->Techonologies;
   } else {
    $val= "";
   }

   if(isset($_POST['projJava'])) {
    $user->Project->Techonologies= $val.",".$_POST['projJava'];
    $user->Project->Techonologies= $_POST['projJava'];
   }
   if(isset($_POST['projPHP'])) {
    $user->Project->Techonologies= $val.",".$_POST['projPHP'];
    $user->Project->Techonologies= $_POST['projPHP'];
   }
   if(isset($_POST['projNet'])) {
    $user->Project->Techonologies= $val.",".$_POST['projNet'];
    $user->Project->Techonologies= $_POST['projNet'];
   }
   if(isset($_POST['projRuby'])) {
    $user->Project->Techonologies= $val.",".$_POST['projRuby'];
    $user->Project->Techonologies= $_POST['projRuby'];
   }
   $this->writeToUsersListFile($jsonContent, $fileName);
  }
  return true;
 }

 function ChangePassword() {
  if(!$this->CheckLogin()) {
   $this->HandleError("Not logged in!");
   return false;
  }

  if(empty($_POST['oldpwd'])) {
   $this->HandleError("Old password is empty!");
   return false;
  }
  if(empty($_POST['newpwd']) || strlen($_REQUEST['newpwd']) <= 8) {
   $this->HandleError("New password is empty and it should have more than eight characters!");
   return false;
  }
  if(($_REQUEST['confPass']) !=($_REQUEST['newpwd'])) {
   $this->HandleError("Passwords are not matching. Please enter same password while confirming");
   return false;
  }

  $user_rec= array();
  //       echo $this->UserE
  if(!$this->GetUserFromEmailInJSON($this->UserEmail(), $user_rec)) {
   return false;
  }

  $pwd= trim($_POST['oldpwd']);
  if($user_rec['pass'] != md5($pwd)) {
   $this->HandleError("The old password does not match!");
   return false;
  }
  $newpwd= trim($_POST['newpwd']);

  if(!$this->ChangePasswordInUserEntry($user_rec, $newpwd)) {
   return false;
  }
  
  // For Community 
   if(chdir ('/var/www/html/cloudbox/work/community/')){
   	$command = 'java -Xmx512m -cp selenium-server-standalone-2.16.1.jar:. cbCommunityChangePassword '.$user_rec['first'].' '.$user_rec['email'].' '.$_POST['oldpwd'].' '.$_POST['newpwd'].' >>changepass.txt 2>&1';
	exec($command);    
   }
  
  
  $this->LogOut();
  return true;
 }

 function ChangePasswordInUserEntry($user_rec, $newpwd) {
  $found= false;

  $fileName= $this->location.$this->usersTempListFileLoc;
 // echo "\nfilename - ".$fileName;
  if(!$this->FromSite()) {
   $fileName= $this->platformUserList;
   //   $fileName= "C:/Leela/reg/userList.json";
  }

  $string= file_get_contents($fileName);
  $jsonContent= json_decode($string);
//  echo "\njsonContent - ".$jsonContent;

  foreach($jsonContent as $user) {
   if($user->email == $user_rec['email']) {
	//echo "\email - ".$user->email;
    $found= true;
	//echo "\nfound - ".$found;
    $user->pass= md5($newpwd);
	//echo "\nnewpass - ".$user->pass;
    $user->resetreqStatus= "Expired";
    $this->writeToUsersListFile($jsonContent, $fileName);

    $this->domain_given= explode("@", $user->email);
    $this->domain_given= $this->domain_given[(count($this->domain_given) - 1)];
    $file= fopen($this->domain_given."_usersList.json", 'w+');
    fwrite($file, $info= json_encode($jsonContent));
    fclose($file);
    break;
   }
  }

  if(!$found) {
   $this->HandleError("Wrong confirm code.");
   return false;
  }
  return true;
 }

 //-------Public Helper functions -------------
 function GetSelfScript() {
  return htmlentities($_SERVER['PHP_SELF']);
 }

 function SafeDisplay($value_name) {
  if(empty($_POST[$value_name])) {
   return '';
  }
  return htmlentities($_POST[$value_name]);
 }

 function RedirectToURL($url) {
  header("Location: $url");
  exit;
 }
 function RedirectToURLAfterTime($url) {
  header("Refresh: 3; url= $url");
  exit;
 }

 function GetSpamTrapInputName() {
  return 'sp'.md5('KHGdnbvsgst'.$this->rand_key);
 }

 function GetErrorMessage() {
  if(empty($this->error_message)) {
   return '';
  }
  $errormsg= nl2br(htmlentities($this->error_message));
  return $errormsg;
 }
 function GetTextMessage() {
  if(empty($this->text_message)) {
   return '';
  }
  $msg= nl2br(htmlentities($this->text_message));
  return $msg;
 }
 //-------Private Helper functions-----------

 function HandleError($err) {
  $this->error_message .= $err."\r\n";
 }
 function GetHTMLText($msg) {
  $this->text_message .= $msg."\r\n";
 }

 function HandleDBError($err) {
  $this->HandleError($err."\r\n mysqlerror:".mysql_error());
 }

 function GetFromAddress() {
  if(!empty($this->from_address)) {
   return $this->from_address;
  }

  $host= $_SERVER['SERVER_NAME'];

  $from= "nobody@$host";
  return $from;
 }

 function GetLoginSessionVar() {
  $retvar= md5($this->rand_key);
  $retvar= 'usr_'.substr($retvar, 0, 10);
  return $retvar;
 }

 function CheckLoginInDB($username, $password) {
  if(!$this->DBLogin()) {
   $this->HandleError("Database login failed!");
   return false;
  }
  $username= $this->SanitizeForSQL($username);
  $pwdmd5= md5($password);
  $qry= "Select name, email from $this->tablename where username='$username' and password='$pwdmd5' and confirmcode='y'";

  $result= mysql_query($qry, $this->connection);

  if(!$result || mysql_num_rows($result) <= 0) {
  // echo "\nDB error\n";
   $this->HandleError("Error logging in. The username or password does not match");
   return false;
  }

  $row= mysql_fetch_assoc($result);

  $_SESSION['name_of_user']= $row['name'];
  $_SESSION['email_of_user']= $row['email'];

  return true;
 }

 function UpdateDBRecForConfirmation_OLD(& $user_rec) {
  if(!$this->DBLogin()) {
   $this->HandleError("Database login failed!");
   return false;
  }
  $confirmcode= $this->SanitizeForSQL($_GET['code']);

  $result= mysql_query("Select name, email from $this->tablename where confirmcode='$confirmcode'", $this->connection);
  if(!$result || mysql_num_rows($result) <= 0) {
   $this->HandleError("Wrong confirm code.");
   return false;
  }
  $row= mysql_fetch_assoc($result);
  $user_rec['name']= $row['name'];
  $user_rec['email']= $row['email'];

  $qry= "Update $this->tablename Set confirmcode='y' Where  confirmcode='$confirmcode'";

  if(!mysql_query($qry, $this->connection)) {
   $this->HandleDBError("Error inserting data to the table\nquery:$qry");
   return false;
  }
  return true;
 }

 function ResetUserPasswordInUserEntry($user_rec) {
  $new_password= substr(md5(uniqid()), 0, 10);

  if(false == $this->ChangePasswordInUserEntry($user_rec, $new_password)) {
   return false;
  }
  return $new_password;
 }

 function ChangePasswordInDB($user_rec, $newpwd) {
  $newpwd= $this->SanitizeForSQL($newpwd);

  $qry= "Update $this->tablename Set password='".md5($newpwd)."' Where  id_user=".$user_rec['id_user']."";

  if(!mysql_query($qry, $this->connection)) {
   $this->HandleDBError("Error updating the password \nquery:$qry");
   return false;
  }
  return true;
 }

 function GetUserFromEmailInJSON($email, & $user_rec) {
  $fileName= $this->location.$this->usersTempListFileLoc;
	if(!$this->FromSite()) {
     		$fileName= $this->platformUserList;
	   //   $fileName= "C:/Leela/reg/userList.json";
  	}

  $usersArray= $this->readUsersListFile($fileName);
  foreach($usersArray as $userInfo) {
   if($userInfo->email == $email) {
//	echo " entered email - ".$email;
	
	    if($userInfo->status != "Active") {
	           $this->HandleError("Your mail id is not confirmed.");
	           return false;
	    }
	    $user_rec= (array) $userInfo;
	    return true;
   }
  }
  $this->HandleError("Sorry... Entered email '".$email."' is not found in our database!");
  return false;
 }

 function GetUserFromEmail($email, & $user_rec) {
  if(!$this->DBLogin()) {
   $this->HandleError("Database login failed!");
   return false;
  }
  $email= $this->SanitizeForSQL($email);

  $result= mysql_query("Select * from $this->tablename where email='$email'", $this->connection);

  if(!$result || mysql_num_rows($result) <= 0) {
   $this->HandleError("There is no user with email: $email");
   return false;
  }
  $user_rec= mysql_fetch_assoc($result);

  return true;
 }

  function SendUserWelcomeEmailPersonal(& $user_rec) {
  $mailer= new PHPMailer();

  $mailer->CharSet= 'utf-8';

  $mailer->AddAddress($user_rec['email'], $user_rec['name']);

  $mailer->Subject= "Welcome to ".$this->sitename;

  $mailer->From= $this->GetFromAddress();

  $url= 'index.php';
  if(!$this->FromSite()) {
   $url= "../cblogin.htm";
  }

  $login_url= $this->GetAbsoluteURLFolder();
  $mailer->IsHTML(true);
  $mailer->Body= "<head>
            <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />
            <title>*|EMAIL SUBJECT|*</title>
    		<link rel=\"stylesheet\" type=\"text/css\" href=\"http:/www.cloudmunch.com/css/mail.css\" />
    	</head>
        <body bgcolor=\"#FFFFFF\" leftmargin=\"0\" marginwidth=\"0\" topmargin=\"0\" marginheight=\"0\" offset=\"0\">
        	<center>
            	<table bgcolor=\"#FFFFFF\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" height=\"100%\" width=\"100%\" id=\"backgroundTable\">
                	<tr>
                    	<td align=\"center\" valign=\"top\">
                            <!-- // Begin Template Preheader \\ -->
                            <table border=\"0\" cellpadding=\"10\" cellspacing=\"0\" width=\"600\" id=\"templatePreheader\">
                                <tr>
                                    <td valign=\"top\" class=\"preheaderContent\">
                                    
                                    	<!-- // Begin Module: Standard Preheader \ -->
                                        <table border=\"0\" cellpadding=\"10\" cellspacing=\"0\" width=\"100%\">
                                        	<tr>
                                            	<td valign=\"top\">
                                                	<div>
                                                    	 
                                                    </div>
                                                </td>
                                                <!-- *|IFNOT:ARCHIVE_PAGE|* -->
    											<td valign=\"top\" width=\"170\">
                                                	<div>
                                                    	
                                                    </div>
                                                </td>
    											<!-- *|END:IF|* -->
                                            </tr>
                                        </table>
                                    	<!-- // End Module: Standard Preheader \ -->
                                        
                                    </td>
                                </tr>
                            </table>
                            <!-- // End Template Preheader \\ -->
                        	<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"600\" id=\"templateContainer\">
                            	<tr>
                                	<td align=\"center\" valign=\"top\">
                                        <!-- // Begin Template Header \\ -->
                                    	<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"600\" id=\"templateHeader\">
                                            <tr>
                                                <td class=\"headerContent\">
                                                
                                                    <!-- // Begin Module: Letterhead, Center Header Image \\ -->
                                                    <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">
                                                        <tr>
                                                        	<td class=\"leftHeaderContent\">
                                                                <div>
                                                                	<a href=\"www.cloudmunch.com\"><img alt=\"CloudMunch Logo\" src=\"http://www.cloudmunch.com/templates/cloudmunchtemplate3/img/cmLogo.gif\" border=\"0\" /></a>
                                                                </div>
                                                            </td>
                                                            <td valign=\"middle\" width=\"180\">
                                                               
                                                            </td>
                                                            <td class=\"rightHeaderContent\">
                                                                <div>
                                                                	
                                                                </div>
                                                            </td>
                                                        </tr>
                                                    </table>
                                                    <!-- // End Module: Letterhead, Center Header Image \\ -->
    
                                                </td>
                                            </tr>
                                        </table>
                                        <!-- // End Template Header \\ -->
                                    </td>
                                </tr>
                            	<tr>
                                	<td align=\"center\" valign=\"top\">
                                        <!-- // Begin Template Body \\ -->
                                    	<table border=\"0\" cellpadding=\"10\" cellspacing=\"0\" width=\"600\" id=\"templateBody\">
                                        	<tr>
                                            	<td valign=\"top\" class=\"bodyContent\">
                                                
                                                    <!-- // Begin Module: Standard Content \\ -->
                                                    <table border=\"0\" cellpadding=\"10\" cellspacing=\"0\" width=\"100%\">
                                                        <tr>
                                                            <td valign=\"top\">
                                                                <div><!-- //////////Add Content Here ////////////// -->
                                                                	
                                                                	<p>Hi ".$user_rec['name'].",</p>
                                                                    <p>A Warm Welcome! Your registration  with ".$this->sitename." is completed. </p>
                                                                    		<p>We trust you will have a rewarding experience on the CloudMunch platform. Please feel free to <a href=\"mailto:feedback@cloudmunch.com?subject=feedback\">get in touch</a> with us for any questions or suggestions and do share your experience with us.\r\n\n </p>
                                                                    	<p>Please log in with your user name and password at : 
                                                                    <br/>                                                            
                                                                    <a href=\"$login_url\" class=\"impLink\">Login Page</a>
                                                                    </p>
                                                                    <br/>
                                                                    <p>
                                                                    	You can login to <a href=\"http://opensource.cloudmunch.com\" class=\"impLink\">opensource.cloudmunch.com</a><br/>
                                                                    	User ID: guest@cloudmunch.com<br/>
                                                                    	Password:password<br/>   
                                                                    </p>
                                                                    <p>Thanks,</p>
                                                                    <p><strong>The CloudMunch Team</strong></p>
    															</div><!-- //////////Content Ends Here ////////////// -->
    														</td>
                                                        </tr>
                                                    </table>
                                                    <!-- // End Module: Standard Content \\ -->
                                                
                                                </td>
                                            </tr>
                                        </table>
                                        <!-- // End Template Body \\ -->
                                    </td>
                                </tr>
                            	<tr>
                                	<td align=\"center\" valign=\"top\">
                                        <!-- // Begin Template Footer \\ -->
                                    	<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"600\" id=\"templateFooter\">
                                        	<tr>
                                            	<td valign=\"top\" class=\"footerContent\">
                                                <hr style=\"width:100%;\" />
                                                    <!-- // Begin Module: Standard Footer \\ -->
                                                    <table border=\"0\" cellpadding=\"10\" cellspacing=\"0\" width=\"100%\">
                                                        <tr>
                                                            <td valign=\"top\" width=\"350\">
                                                                <div>
    																<em>Copyright &copy; 2012 CloudMunch, All rights reserved.</em>
    														
    																</div>
                                                            </td>
                                                            <td valign=\"top\" width=\"190\" id=\"monkeyRewards\">
                                                                <div>
                                                                    You are recieving this email because you have registered with CloudMunch. If you think you have recieved this email by error, please send a mail with your details to support@cloudmunch.com to unsubscribe.
    
                                                                
                                                            </td>
                                                        </tr>
    
                                                         </table>
                                                    <!-- // End Module: Standard Footer \\ -->
                                                
                                                </td>
                                            </tr>
                                        </table>
                                        <!-- // End Template Footer \\ -->
                                    </td>
                                </tr>
                            </table>
                            <br />
                        </td>
                    </tr>
                </table>
            </center>
        </body>
    </html>";
  //  $mailer->Body= "Hello ".$user_rec['name'].",\r\n\r\n".
  //  "A warm welcome! Your registration with Cloudmunch is now complete.\r\n\n".
  //  "Please log in with your user name and password at: ".$this->GetAbsoluteURLFolder().$url."\r\n We trust you will have a rewarding privileged beta experience on the Cloudmunch platform. Please feel free to get in touch with us for any questions or suggestions and do share your experience with us.\r\n\n".
  //  "Cloudmunch's All-in-One Application delivery platform gives you all the tools you need to deliver great application with faster releases, better quality and lower costs to create a competitive advantage. Our goal is to enable you to focus on writing great code while Cloudmunch takes care of the rest.\r\n".
  //  "Best Regards,\n".
  //  "The Cloudmunch Team\n".
  //  	"www.cloudmunch.com";

  //  $mailer->Body= "Hello ".$user_rec['name']."\r\n\r\n".
  //  "Welcome! Your registration  with ".$this->sitename." is completed.\r\n".
  //  "Login here: ".$this->GetAbsoluteURLFolder().$url."\r\n".
  //  "\r\n".
  //  "Regards,\r\n".
  //  "Webmaster\r\n".
  //  $this->sitename;

  if(!$mailer->Send()) {
   $this->HandleError("Failed sending user welcome email.");
   return false;
  }
  return true;
 }



 function SendUserWelcomeEmail(& $user_rec) {
  $mailer= new PHPMailer();

  $mailer->CharSet= 'utf-8';

  $mailer->AddAddress($user_rec['email'], $user_rec['name']);

  $mailer->Subject= "Welcome to ".$this->sitename;

  $mailer->From= $this->GetFromAddress();

  $url= 'index.php';
  if(!$this->FromSite()) {
   $url= "../cblogin.htm";
  }

  $login_url= $this->GetAbsoluteURLFolder();
  $mailer->IsHTML(true);
  $mailer->Body= "<head>
            <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />
            <title>*|EMAIL SUBJECT|*</title>
    		<link rel=\"stylesheet\" type=\"text/css\" href=\"http:/www.cloudmunch.com/css/mail.css\" />
    	</head>
        <body bgcolor=\"#FFFFFF\" leftmargin=\"0\" marginwidth=\"0\" topmargin=\"0\" marginheight=\"0\" offset=\"0\">
        	<center>
            	<table bgcolor=\"#FFFFFF\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" height=\"100%\" width=\"100%\" id=\"backgroundTable\">
                	<tr>
                    	<td align=\"center\" valign=\"top\">
                            <!-- // Begin Template Preheader \\ -->
                            <table border=\"0\" cellpadding=\"10\" cellspacing=\"0\" width=\"600\" id=\"templatePreheader\">
                                <tr>
                                    <td valign=\"top\" class=\"preheaderContent\">
                                    
                                    	<!-- // Begin Module: Standard Preheader \ -->
                                        <table border=\"0\" cellpadding=\"10\" cellspacing=\"0\" width=\"100%\">
                                        	<tr>
                                            	<td valign=\"top\">
                                                	<div>
                                                    	 
                                                    </div>
                                                </td>
                                                <!-- *|IFNOT:ARCHIVE_PAGE|* -->
    											<td valign=\"top\" width=\"170\">
                                                	<div>
                                                    	
                                                    </div>
                                                </td>
    											<!-- *|END:IF|* -->
                                            </tr>
                                        </table>
                                    	<!-- // End Module: Standard Preheader \ -->
                                        
                                    </td>
                                </tr>
                            </table>
                            <!-- // End Template Preheader \\ -->
                        	<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"600\" id=\"templateContainer\">
                            	<tr>
                                	<td align=\"center\" valign=\"top\">
                                        <!-- // Begin Template Header \\ -->
                                    	<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"600\" id=\"templateHeader\">
                                            <tr>
                                                <td class=\"headerContent\">
                                                
                                                    <!-- // Begin Module: Letterhead, Center Header Image \\ -->
                                                    <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">
                                                        <tr>
                                                        	<td class=\"leftHeaderContent\">
                                                                <div>
                                                                	<a href=\"www.cloudmunch.com\"><img alt=\"CloudMunch Logo\" src=\"http://www.cloudmunch.com/templates/cloudmunchtemplate3/img/cmLogo.gif\" border=\"0\" /></a>
                                                                </div>
                                                            </td>
                                                            <td valign=\"middle\" width=\"180\">
                                                               
                                                            </td>
                                                            <td class=\"rightHeaderContent\">
                                                                <div>
                                                                	
                                                                </div>
                                                            </td>
                                                        </tr>
                                                    </table>
                                                    <!-- // End Module: Letterhead, Center Header Image \\ -->
    
                                                </td>
                                            </tr>
                                        </table>
                                        <!-- // End Template Header \\ -->
                                    </td>
                                </tr>
                            	<tr>
                                	<td align=\"center\" valign=\"top\">
                                        <!-- // Begin Template Body \\ -->
                                    	<table border=\"0\" cellpadding=\"10\" cellspacing=\"0\" width=\"600\" id=\"templateBody\">
                                        	<tr>
                                            	<td valign=\"top\" class=\"bodyContent\">
                                                
                                                    <!-- // Begin Module: Standard Content \\ -->
                                                    <table border=\"0\" cellpadding=\"10\" cellspacing=\"0\" width=\"100%\">
                                                        <tr>
                                                            <td valign=\"top\">
                                                                <div><!-- //////////Add Content Here ////////////// -->
                                                                	
                                                                	<p>Hi ".$user_rec['name'].",</p>
                                                                    <p>A Warm Welcome! Your registration  with ".$this->sitename." is completed. </p>
                                                                    		<p>We trust you will have a rewarding experience on the CloudMunch platform. Please feel free to <a href=\"mailto:feedback@cloudmunch.com?subject=feedback\">get in touch</a> with us for any questions or suggestions and do share your experience with us.\r\n\n </p>
                                                                    	<p>Please log in with your user name and password at : 
                                                                    <br/>                                                            
                                                                    <a href=\"$login_url\" class=\"impLink\">Login Page</a>
                                                                    </p>
                                                                    <br/>
                                                                    <p>Thanks,</p>
                                                                    <p><strong>The CloudMunch Team</strong></p>
    															</div><!-- //////////Content Ends Here ////////////// -->
    														</td>
                                                        </tr>
                                                    </table>
                                                    <!-- // End Module: Standard Content \\ -->
                                                
                                                </td>
                                            </tr>
                                        </table>
                                        <!-- // End Template Body \\ -->
                                    </td>
                                </tr>
                            	<tr>
                                	<td align=\"center\" valign=\"top\">
                                        <!-- // Begin Template Footer \\ -->
                                    	<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"600\" id=\"templateFooter\">
                                        	<tr>
                                            	<td valign=\"top\" class=\"footerContent\">
                                                <hr style=\"width:100%;\" />
                                                    <!-- // Begin Module: Standard Footer \\ -->
                                                    <table border=\"0\" cellpadding=\"10\" cellspacing=\"0\" width=\"100%\">
                                                        <tr>
                                                            <td valign=\"top\" width=\"350\">
                                                                <div>
    																<em>Copyright &copy; 2012 CloudMunch, All rights reserved.</em>
    														
    																</div>
                                                            </td>
                                                            <td valign=\"top\" width=\"190\" id=\"monkeyRewards\">
                                                                <div>
                                                                    You are recieving this email because you have registered with CloudMunch. If you think you have recieved this email by error, please send a mail with your details to support@cloudmunch.com to unsubscribe.
    
                                                                
                                                            </td>
                                                        </tr>
    
                                                         </table>
                                                    <!-- // End Module: Standard Footer \\ -->
                                                
                                                </td>
                                            </tr>
                                        </table>
                                        <!-- // End Template Footer \\ -->
                                    </td>
                                </tr>
                            </table>
                            <br />
                        </td>
                    </tr>
                </table>
            </center>
        </body>
    </html>";
  //  $mailer->Body= "Hello ".$user_rec['name'].",\r\n\r\n".
  //  "A warm welcome! Your registration with Cloudmunch is now complete.\r\n\n".
  //  "Please log in with your user name and password at: ".$this->GetAbsoluteURLFolder().$url."\r\n We trust you will have a rewarding privileged beta experience on the Cloudmunch platform. Please feel free to get in touch with us for any questions or suggestions and do share your experience with us.\r\n\n".
  //  "Cloudmunch's All-in-One Application delivery platform gives you all the tools you need to deliver great application with faster releases, better quality and lower costs to create a competitive advantage. Our goal is to enable you to focus on writing great code while Cloudmunch takes care of the rest.\r\n".
  //  "Best Regards,\n".
  //  "The Cloudmunch Team\n".
  //  	"www.cloudmunch.com";

  //  $mailer->Body= "Hello ".$user_rec['name']."\r\n\r\n".
  //  "Welcome! Your registration  with ".$this->sitename." is completed.\r\n".
  //  "Login here: ".$this->GetAbsoluteURLFolder().$url."\r\n".
  //  "\r\n".
  //  "Regards,\r\n".
  //  "Webmaster\r\n".
  //  $this->sitename;

  if(!$mailer->Send()) {
   $this->HandleError("Failed sending user welcome email.");
   return false;
  }
  return true;
 }

 function SendAdminIntimationOnRegComplete(& $user_rec) {
  if(empty($this->admin_email)) {
   return false;
  }
  $mailer= new PHPMailer();

  $mailer->CharSet= 'utf-8';

  $mailer->AddAddress($this->admin_email);

  $mailer->Subject= "Registration Completed: ".$user_rec['name'];

  $mailer->From= $this->GetFromAddress();

  $mailer->Body= "A new user registered at ".$this->sitename."\r\n".
  "Name: ".$user_rec['name']."\r\n".
  "Email address: ".$user_rec['email']."\r\n".
  "Purpose of registration: ".$user_rec['purpose']."\r\n".
  "Number: ".$user_rec['number']."\r\n".
  "IP Address: ".$user_rec['ip_user'];

  if(!$mailer->Send()) {
   return false;
  }
  return true;
 }

 function GetResetPasswordCode($email) {
  $randno1= rand();
  $randno2= rand();
  return md5($email.$this->rand_key.$randno1.''.$randno2);
 }

 function SendResetPasswordLink($user_rec) {
  $email= $user_rec['email'];

  $mailer= new PHPMailer();

  $mailer->CharSet= 'utf-8';

  $mailer->AddAddress($email, $user_rec['first']);

  $mailer->Subject= "Your reset password request at ".$this->sitename;

  $mailer->From= $this->GetFromAddress();

  $resetCode= $this->GetResetPasswordCode($email);

  $file= $this->location.$this->usersTempListFileLoc;
  if(!$this->FromSite()) {
   $file= $this->platformUserList;
   //   $file= "C:/Leela/reg/userlist.json";
  }

  $string= file_get_contents($file);
  $jsonContent= json_decode($string);

  $user= $jsonContent-> $_REQUEST['email'];
  $user->resetCode= $resetCode;
  $user->resetreqDate= date("Y-m-d-H:i:s");
  $user->resetreqStatus= "Active";

  $this->writeToUsersListFile($jsonContent, $file);
//$this->GetAbsoluteURLFolder()
//http://ec2-23-22-203-148.compute-1.amazonaws.comindex.php/?option=com_content&view=article&id=144&email=shraddha%40cloudboxonline.com&code=1070bc0ad8bc0deda43d0a44782ea5ee
  $link= $_SERVER['HTTP_HOST'].'/index.php/changepassword?reset=true&email='.urlencode($email).'&code='.$resetCode;
  //'/index.php/reset-pwd?email='.
  //urlencode($email).'&code='.
  //$resetCode;
  $mailer->IsHTML(true);
  $mailer->Body= "<head>
            <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />
            <title>*|EMAIL SUBJECT|*</title>
    		<link rel=\"stylesheet\" type=\"text/css\" href=\"http:/www.cloudmunch.com/css/mail.css\" />
    	</head>
        <body bgcolor=\"#FFFFFF\" leftmargin=\"0\" marginwidth=\"0\" topmargin=\"0\" marginheight=\"0\" offset=\"0\">
        	<center>
            	<table bgcolor=\"#FFFFFF\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" height=\"100%\" width=\"100%\" id=\"backgroundTable\">
                	<tr>
                    	<td align=\"center\" valign=\"top\">
                            <!-- // Begin Template Preheader \\ -->
                            <table border=\"0\" cellpadding=\"10\" cellspacing=\"0\" width=\"600\" id=\"templatePreheader\">
                                <tr>
                                    <td valign=\"top\" class=\"preheaderContent\">
                                    
                                    	<!-- // Begin Module: Standard Preheader \ -->
                                        <table border=\"0\" cellpadding=\"10\" cellspacing=\"0\" width=\"100%\">
                                        	<tr>
                                            	<td valign=\"top\">
                                                	<div>
                                                    	 
                                                    </div>
                                                </td>
                                                <!-- *|IFNOT:ARCHIVE_PAGE|* -->
    											<td valign=\"top\" width=\"170\">
                                                	<div>
                                                    	
                                                    </div>
                                                </td>
    											<!-- *|END:IF|* -->
                                            </tr>
                                        </table>
                                    	<!-- // End Module: Standard Preheader \ -->
                                        
                                    </td>
                                </tr>
                            </table>
                            <!-- // End Template Preheader \\ -->
                        	<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"600\" id=\"templateContainer\">
                            	<tr>
                                	<td align=\"center\" valign=\"top\">
                                        <!-- // Begin Template Header \\ -->
                                    	<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"600\" id=\"templateHeader\">
                                            <tr>
                                                <td class=\"headerContent\">
                                                
                                                    <!-- // Begin Module: Letterhead, Center Header Image \\ -->
                                                    <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">
                                                        <tr>
                                                        	<td class=\"leftHeaderContent\">
                                                                <div>
                                                                	<a href=\"www.cloudmunch.com\"><img alt=\"Cloud Munch Logo\" src=\"http://www.cloudmunch.com/templates/cloudmunchtemplate3/img/cmLogo.gif\" border=\"0\" /></a>
                                                                </div>
                                                            </td>
                                                            <td valign=\"middle\" width=\"180\">
                                                               
                                                            </td>
                                                            <td class=\"rightHeaderContent\">
                                                                <div>
                                                                	
                                                                </div>
                                                            </td>
                                                        </tr>
                                                    </table>
                                                    <!-- // End Module: Letterhead, Center Header Image \\ -->
    
                                                </td>
                                            </tr>
                                        </table>
                                        <!-- // End Template Header \\ -->
                                    </td>
                                </tr>
                            	<tr>
                                	<td align=\"center\" valign=\"top\">
                                        <!-- // Begin Template Body \\ -->
                                    	<table border=\"0\" cellpadding=\"10\" cellspacing=\"0\" width=\"600\" id=\"templateBody\">
                                        	<tr>
                                            	<td valign=\"top\" class=\"bodyContent\">
                                                
                                                    <!-- // Begin Module: Standard Content \\ -->
                                                    <table border=\"0\" cellpadding=\"10\" cellspacing=\"0\" width=\"100%\">
                                                        <tr>
                                                            <td valign=\"top\">
                                                                <div><!-- //////////Add Content Here ////////////// -->
                                                                	
                                                                	<p>Hi ".$user_rec['first'].",</p>
                                                                    <p>There was a request to reset your password at ".$this->sitename."</p>
                                                                    	<p>Please click the link below to complete the request:</p>
                                                                    <br/>                                                            
                                                                    <a href=\"$link\" class=\"impLink\">Reset Your Password</a>
                                                                    <br/>
                                                                    <p>Thanks,</p>
                                                                    <p><strong>The CloudMunch Team</strong></p>
    															</div><!-- //////////Content Ends Here ////////////// -->
    														</td>
                                                        </tr>
                                                    </table>
                                                    <!-- // End Module: Standard Content \\ -->
                                                
                                                </td>
                                            </tr>
                                        </table>
                                        <!-- // End Template Body \\ -->
                                    </td>
                                </tr>
                            	<tr>
                                	<td align=\"center\" valign=\"top\">
                                        <!-- // Begin Template Footer \\ -->
                                    	<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"600\" id=\"templateFooter\">
                                        	<tr>
                                            	<td valign=\"top\" class=\"footerContent\">
                                                <hr style=\"width:100%;\" />
                                                    <!-- // Begin Module: Standard Footer \\ -->
                                                    <table border=\"0\" cellpadding=\"10\" cellspacing=\"0\" width=\"100%\">
                                                        <tr>
                                                            <td valign=\"top\" width=\"350\">
                                                                <div>
    																<em>Copyright &copy; 2012 CloudMunch, All rights reserved.</em>
    														
    																</div>
                                                            </td>
                                                            <td valign=\"top\" width=\"190\" id=\"monkeyRewards\">
                                                                <div>
                                                                    You are recieving this email because you have registered with CloudMunch . If you think you have recieved this email by error, please send a mail with your details to support@cloudmunch.com to unsubscribe.
    
                                                                
                                                            </td>
                                                        </tr>
    
                                                         </table>
                                                    <!-- // End Module: Standard Footer \\ -->
                                                
                                                </td>
                                            </tr>
                                        </table>
                                        <!-- // End Template Footer \\ -->
                                    </td>
                                </tr>
                            </table>
                            <br />
                        </td>
                    </tr>
                </table>
            </center>
        </body>
    </html>";

  //  $mailer->Body= "Hello ".$user_rec['first']."\r\n\r\n".
  //  "There was a request to reset your password at ".$this->sitename."\r\n".
  //  "Please click the link below to complete the request: \r\n".$link."\r\n".
  //  "Regards,\r\n".
  //  "The Cloudmunch Team \r\n".
  //  $this->sitename;

  if(!$mailer->Send()) {
   $this->HandleError("Link to reset your password cannot be mailed to ".$email);
   return false;
  }
  return true;
 }

 function SendNewPassword($user_rec, $new_password) {
  $email= $user_rec['email'];

  $mailer= new PHPMailer();

  $mailer->CharSet= 'utf-8';

  $mailer->AddAddress($email, $user_rec['first']);

  $mailer->Subject= "Your new password for ".$this->sitename;

  $mailer->From= $this->GetFromAddress();
  $mailer->IsHTML(true);
  
   $url= 'index.php';
  if(!$this->FromSite()) {
   $url= "../cblogin.htm";
  }
  
  
  $login_url= $this->GetAbsoluteURLFolder();
  $mailer->Body= "<head>
            <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />
            <title>*|EMAIL SUBJECT|*</title>
    		<link rel=\"stylesheet\" type=\"text/css\" href=\"http:/www.cloudmunch.com/css/mail.css\" />
    	</head>
        <body bgcolor=\"#FFFFFF\" leftmargin=\"0\" marginwidth=\"0\" topmargin=\"0\" marginheight=\"0\" offset=\"0\">
        	<center>
            	<table bgcolor=\"#FFFFFF\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" height=\"100%\" width=\"100%\" id=\"backgroundTable\">
                	<tr>
                    	<td align=\"center\" valign=\"top\">
                            <!-- // Begin Template Preheader \\ -->
                            <table border=\"0\" cellpadding=\"10\" cellspacing=\"0\" width=\"600\" id=\"templatePreheader\">
                                <tr>
                                    <td valign=\"top\" class=\"preheaderContent\">
                                    
                                    	<!-- // Begin Module: Standard Preheader \ -->
                                        <table border=\"0\" cellpadding=\"10\" cellspacing=\"0\" width=\"100%\">
                                        	<tr>
                                            	<td valign=\"top\">
                                                	<div>
                                                    	 
                                                    </div>
                                                </td>
                                                <!-- *|IFNOT:ARCHIVE_PAGE|* -->
    											<td valign=\"top\" width=\"170\">
                                                	<div>
                                                    	
                                                    </div>
                                                </td>
    											<!-- *|END:IF|* -->
                                            </tr>
                                        </table>
                                    	<!-- // End Module: Standard Preheader \ -->
                                        
                                    </td>
                                </tr>
                            </table>
                            <!-- // End Template Preheader \\ -->
                        	<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"600\" id=\"templateContainer\">
                            	<tr>
                                	<td align=\"center\" valign=\"top\">
                                        <!-- // Begin Template Header \\ -->
                                    	<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"600\" id=\"templateHeader\">
                                            <tr>
                                                <td class=\"headerContent\">
                                                
                                                    <!-- // Begin Module: Letterhead, Center Header Image \\ -->
                                                    <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">
                                                        <tr>
                                                        	<td class=\"leftHeaderContent\">
                                                                <div>
                                                                	<a href=\"www.cloudmunch.com\"><img alt=\"CloudMunch Logo\" src=\"http://www.cloudmunch.com/templates/cloudmunchtemplate3/img/cmLogo.gif\" border=\"0\" /></a>
                                                                </div>
                                                            </td>
                                                            <td valign=\"middle\" width=\"180\">
                                                               
                                                            </td>
                                                            <td class=\"rightHeaderContent\">
                                                                <div>
                                                                	
                                                                </div>
                                                            </td>
                                                        </tr>
                                                    </table>
                                                    <!-- // End Module: Letterhead, Center Header Image \\ -->
    
                                                </td>
                                            </tr>
                                        </table>
                                        <!-- // End Template Header \\ -->
                                    </td>
                                </tr>
                            	<tr>
                                	<td align=\"center\" valign=\"top\">
                                        <!-- // Begin Template Body \\ -->
                                    	<table border=\"0\" cellpadding=\"10\" cellspacing=\"0\" width=\"600\" id=\"templateBody\">
                                        	<tr>
                                            	<td valign=\"top\" class=\"bodyContent\">
                                                
                                                    <!-- // Begin Module: Standard Content \\ -->
                                                    <table border=\"0\" cellpadding=\"10\" cellspacing=\"0\" width=\"100%\">
                                                        <tr>
                                                            <td valign=\"top\">
                                                                <div><!-- //////////Add Content Here ////////////// -->
                                                                	
                                                                	<p>Hi ".$user_rec['first'].",</p>
                                                                    <p>Your password is reset successfully. </p>
                                                                    	<p>Here is your updated login:</p><p>username: ".$user_rec['email']."</p>
      <p>password:".$new_password."</p><br/>                                                            
                                                                    <a href=\"$login_url\" class=\"impLink\">Click here to Login</a>
                                                                    <br/>                                                            
                                                                    <br/>
                                                                    <p>Thanks,</p>
                                                                    <p><strong>The CloudMunch Team</strong></p>
    															</div><!-- //////////Content Ends Here ////////////// -->
    														</td>
                                                        </tr>
                                                    </table>
                                                    <!-- // End Module: Standard Content \\ -->
                                                
                                                </td>
                                            </tr>
                                        </table>
                                        <!-- // End Template Body \\ -->
                                    </td>
                                </tr>
                            	<tr>
                                	<td align=\"center\" valign=\"top\">
                                        <!-- // Begin Template Footer \\ -->
                                    	<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"600\" id=\"templateFooter\">
                                        	<tr>
                                            	<td valign=\"top\" class=\"footerContent\">
                                                <hr style=\"width:100%;\" />
                                                    <!-- // Begin Module: Standard Footer \\ -->
                                                    <table border=\"0\" cellpadding=\"10\" cellspacing=\"0\" width=\"100%\">
                                                        <tr>
                                                            <td valign=\"top\" width=\"350\">
                                                                <div>
    																<em>Copyright &copy; 2012 CloudMunch, All rights reserved.</em>
    														
    																</div>
                                                            </td>
                                                            <td valign=\"top\" width=\"190\" id=\"monkeyRewards\">
                                                                <div>
                                                                    You are recieving this email because you have registered with CloudMunch. If you think you have recieved this email by error, please send a mail with your details to support@cloudmunch.com to unsubscribe.
    
                                                                
                                                            </td>
                                                        </tr>
    
                                                         </table>
                                                    <!-- // End Module: Standard Footer \\ -->
                                                
                                                </td>
                                            </tr>
                                        </table>
                                        <!-- // End Template Footer \\ -->
                                    </td>
                                </tr>
                            </table>
                            <br />
                        </td>
                    </tr>
                </table>
            </center>
        </body>
    </html>";

  //  $mailer->Body= "Hello ".$user_rec['first']."\r\n\r\n".
  //  "Your password is reset successfully. ".
  //  "Here is your updated login:\r\n".
  //  "username: ".$user_rec['email']."\r\n".
  //  "password: ".$new_password."\r\n".
  //  "\r\n".
  //  "Login here: ".$this->GetAbsoluteURLFolder()."/index.php\r\n".
  //  "\r\n".
  //  "Regards,\r\n".
  //  "The Cloudmunch Team\r\n".
  //  $this->sitename;

  if(!$mailer->Send()) {
   return false;
  }
  return true;
 }

 function ValidateRegistrationSubmission() {
  //This is a hidden input field. Humans won't fill this field.
  if(!empty($_POST[$this->GetSpamTrapInputName()])) {
   //The proper error is not given intentionally
   $this->HandleError("Automated submission prevention: case 2 failed");
   return false;
  }

  $validator= new FormValidator();
  $validator->addValidation("name", "req", "Please fill in your First Name.");
  $validator->addValidation("lastname", "req", "Please fill in your Last Name.");
  $validator->addValidation("email", "email", "The input for Email should be a valid email value.");
  $validator->addValidation("email", "req", "Please fill in your Corporate Email.");
  $validator->addValidation("Company", "req", "Please fill in your Company Name.");
  error_log("before validating radio");
  $validator->addValidation("purpose", "req", "Please select one of the Evaluation options.");
  error_log("after validating radio");

  if(!$validator->ValidateForm()) {
   $error= '';
   $error_hash= $validator->GetErrors();
   foreach($error_hash as $inpname => $inp_err) {
    $error .= $inp_err."\n";
   }
   $this->HandleError($error);
   return false;
  }
  return true;
 }


	
	


 function CollectRegistrationOnConfirm(& $formvars) {
  $formvars['username']= $this->Sanitize($_POST['username']);
  $formvars['password']= $this->Sanitize($_POST['password']);
 }
 function CollectRegistrationSubmission(& $formvars) {
  //                 	echo "val of name - ".$_POST['name'];
  $formvars['name']= $this->Sanitize($_POST['name']);
  $formvars['lastname']= $this->Sanitize($_POST['lastname']);
  $formvars['email']= $this->Sanitize($_POST['email']);
  $formvars['Company']= $this->Sanitize($_POST['Company']);
  $formvars['ip']= $this->Sanitize($_POST['ip']);
  $formvars['purpose']= $this->Sanitize($_POST['purpose']);

  if(!(is_numeric($_POST['number']))){
	if (preg_match("/[^\d\s \(\)+-]/", $_POST['number'])) {
    	//Invalid
    		error_log("is not numeric");
  		$formvars['number']= "invalid";
	}
	else{
		$formvars['number']= $this->Sanitize($_POST['number']);
  		error_log("is numeric - ".$formvars['number']);
	}
  }

    if (isset($_REQUEST['f6s'])){
    error_log("f6s is set");
    $formvars['f6s']= $this->Sanitize($_POST['f6s']);
  }
  else{
   error_log("f6s not set");
  }
 
 }


 function SendUserConfirmationEmail(& $formvars) {
  include_once('class.smtp.php');
  $mailer= new PHPMailer();

  $mailer->CharSet= 'utf-8';

  $mailer->AddAddress($formvars['email'], $formvars['name']);

  $mailer->Subject= "Your registration with ".$this->sitename;

  $mailer->From= $this->GetFromAddress();

  $confirmcode= $formvars['confirmcode'];

  $confirm_url= $this->GetAbsoluteURLFolder().'/index.php/confirm-registration?code='.$confirmcode."&email=".$formvars['email'];

  $mailer->IsHTML(true);

  $mailer->Body= "<head>
            <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />
            <title>*|EMAIL SUBJECT|*</title>
    		<link rel=\"stylesheet\" type=\"text/css\" href=\"http://www.cloudmunch.com/css/mail.css\" />
    	</head>
        <body bgcolor=\"#FFFFFF\" leftmargin=\"0\" marginwidth=\"0\" topmargin=\"0\" marginheight=\"0\" offset=\"0\">
        	<center>
            	<table bgcolor=\"#FFFFFF\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" height=\"100%\" width=\"100%\" id=\"backgroundTable\">
                	<tr>
                    	<td align=\"center\" valign=\"top\">
                            <!-- // Begin Template Preheader \\ -->
                            <table border=\"0\" cellpadding=\"10\" cellspacing=\"0\" width=\"600\" id=\"templatePreheader\">
                                <tr>
                                    <td valign=\"top\" class=\"preheaderContent\">
                                    
                                    	<!-- // Begin Module: Standard Preheader \ -->
                                        <table border=\"0\" cellpadding=\"10\" cellspacing=\"0\" width=\"100%\">
                                        	<tr>
                                            	<td valign=\"top\">
                                                	<div>
                                                    	 
                                                    </div>
                                                </td>
                                                <!-- *|IFNOT:ARCHIVE_PAGE|* -->
    											<td valign=\"top\" width=\"170\">
                                                	<div>
                                                    	
                                                    </div>
                                                </td>
    											<!-- *|END:IF|* -->
                                            </tr>
                                        </table>
                                    	<!-- // End Module: Standard Preheader \ -->
                                        
                                    </td>
                                </tr>
                            </table>
                            <!-- // End Template Preheader \\ -->
                        	<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"600\" id=\"templateContainer\">
                            	<tr>
                                	<td align=\"center\" valign=\"top\">
                                        <!-- // Begin Template Header \\ -->
                                    	<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"600\" id=\"templateHeader\">
                                            <tr>
                                                <td class=\"headerContent\">
                                                
                                                    <!-- // Begin Module: Letterhead, Center Header Image \\ -->
                                                    <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">
                                                        <tr>
                                                        	<td class=\"leftHeaderContent\">
                                                                <div>
                                                                	<a href=\"www.cloudmunch.com\"><img alt=\"Cloud Munch Logo\" src=\"http://www.cloudmunch.com/templates/cloudmunchtemplate3/img/cmLogo.gif\" border=\"0\" /></a>
                                                                </div>
                                                            </td>
                                                            <td valign=\"middle\" width=\"180\">
                                                               
                                                            </td>
                                                            <td class=\"rightHeaderContent\">
                                                                <div>
                                                                	
                                                                </div>
                                                            </td>
                                                        </tr>
                                                    </table>
                                                    <!-- // End Module: Letterhead, Center Header Image \\ -->
    
                                                </td>
                                            </tr>
                                        </table>
                                        <!-- // End Template Header \\ -->
                                    </td>
                                </tr>
                            	<tr>
                                	<td align=\"center\" valign=\"top\">
                                        <!-- // Begin Template Body \\ -->
                                    	<table border=\"0\" cellpadding=\"10\" cellspacing=\"0\" width=\"600\" id=\"templateBody\">
                                        	<tr>
                                            	<td valign=\"top\" class=\"bodyContent\">
                                                
                                                    <!-- // Begin Module: Standard Content \\ -->
                                                    <table border=\"0\" cellpadding=\"10\" cellspacing=\"0\" width=\"100%\">
                                                        <tr>
                                                            <td valign=\"top\">
                                                                <div><!-- //////////Add Content Here ////////////// -->
                                                                	
                                                                	<p>Hi ".
  $formvars['name'].",</p>
                                                                    <p>Thank you for registering with ".$this->sitename."</p>
                                                                    	<p>Please click the link below to confirm your registration.</p>
                                                                    <br/>                                                            
                                                                    <a href=\"$confirm_url\" class=\"impLink\">Confirm Your Registration</a>
                                                                    <br/>
                                                                    <p>Thanks,</p>
                                                                    <p><strong>The CloudMunch Team</strong></p>
    															</div><!-- //////////Content Ends Here ////////////// -->
    														</td>
                                                        </tr>
                                                    </table>
                                                    <!-- // End Module: Standard Content \\ -->
                                                
                                                </td>
                                            </tr>
                                        </table>
                                        <!-- // End Template Body \\ -->
                                    </td>
                                </tr>
                            	<tr>
                                	<td align=\"center\" valign=\"top\">
                                        <!-- // Begin Template Footer \\ -->
                                    	<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"600\" id=\"templateFooter\">
                                        	<tr>
                                            	<td valign=\"top\" class=\"footerContent\">
                                                <hr style=\"width:100%;\" />
                                                    <!-- // Begin Module: Standard Footer \\ -->
                                                    <table border=\"0\" cellpadding=\"10\" cellspacing=\"0\" width=\"100%\">
                                                        <tr>
                                                            <td valign=\"top\" width=\"350\">
                                                                <div>
    																<em>Copyright &copy; 2012 CloudMunch, All rights reserved.</em>
    														
    																</div>
                                                            </td>
                                                            <td valign=\"top\" width=\"190\" id=\"monkeyRewards\">
                                                                <div>
                                                                    You are recieving this email because you are registering with CloudMunch. If you think you have recieved this email by error, please send a mail with your details to support@cloudmunch.com to unsubscribe.
    
                                                                
                                                            </td>
                                                        </tr>
    
                                                         </table>
                                                    <!-- // End Module: Standard Footer \\ -->
                                                
                                                </td>
                                            </tr>
                                        </table>
                                        <!-- // End Template Footer \\ -->
                                    </td>
                                </tr>
                            </table>
                            <br />
                        </td>
                    </tr>
                </table>
            </center>
        </body>
    </html>";

  //  $mailer->Body= "Hello ".$formvars['name']."\r\n\r\n".
  //  "Thanks for your registration with ".$this->sitename."\r\n".
  //  "Please click the link below to confirm your registration.\r\n".
  //  "$confirm_url\r\n".
  //  "\r\n".
  //  "Regards,\r\n".
  //  "Webmaster\r\n".
  //  $this->sitename;

  if(!$mailer->Send()) {
   $this->HandleError("Failed sending registration confirmation email to ".$formvars['email']);
   return false;
  }
  return true;
 }
 function SendUserConfirmationEmailOld(& $formvars) {
  include_once('class.smtp.php');
  $mailer= new PHPMailer();

  $mailer->CharSet= 'utf-8';

  $mailer->AddAddress($formvars['email'], $formvars['name']);

  $mailer->Subject= "Your registration with ".$this->sitename;

  $mailer->From= $this->GetFromAddress();

  $confirmcode= $formvars['confirmcode'];

  $confirm_url= $this->GetAbsoluteURLFolder().'index.php/confirm-registration?code='.$confirmcode."&email=".$formvars['email'];

  $mailer->Body= "Hello ".$formvars['name'].",\r\n\r\n".
  "Thank you for registering with Cloudmunch.".
  "Please click on the link below to confirm your registration. \r\n\r\n".
  "$confirm_url\r\n\n".
  "We welcome you to a rewarding experience on the CloudMunch platform. Please feel free to get in touch  with us if you have any questions or suggestions.\r\n\n".
  "CloudMunch's All-in-One application delivery platform gives you all the tools you need to deliver great application with faster releases, better quality and lower costs to create a competitive advantage. Our goal is to enable you to focus on writing great code while CloudMunch takes care of the rest. \r\n\r\n".
  "Best Regards,
      The CloudMunch Team
      www.cloudmunch.com";

  //  $mailer->Body= "Hello ".$formvars['name']."\r\n\r\n".
  //  "Thanks for your registration with ".$this->sitename."\r\n".
  //  "Please click the link below to confirm your registration.\r\n".
  //  "$confirm_url\r\n".
  //  "\r\n".
  //  "Regards,\r\n".
  //  "Webmaster\r\n".
  //  $this->sitename;

  if(!$mailer->Send()) {
   $this->HandleError("Failed sending registration confirmation email to ".$formvars['email']);
   return false;
  }
  return true;
 }

 function SendIneligibleUserEmail(& $formvars) {
  include_once('class.smtp.php');
  $mailer= new PHPMailer();

  $mailer->CharSet= 'utf-8';

  $mailer->AddAddress($formvars['email'], $formvars['name']);

  $mailer->Subject= "Your interest in ".$this->sitename;

  $mailer->From= $this->GetFromAddress();

  $mailer->IsHTML(true);
  $mailer->Body= "<head>
            <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />
            <title>*|EMAIL SUBJECT|*</title>
    		<link rel=\"stylesheet\" type=\"text/css\" href=\"http:/www.cloudmunch.com/css/mail.css\" />
    	</head>
        <body bgcolor=\"#FFFFFF\" leftmargin=\"0\" marginwidth=\"0\" topmargin=\"0\" marginheight=\"0\" offset=\"0\">
        	<center>
            	<table bgcolor=\"#FFFFFF\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" height=\"100%\" width=\"100%\" id=\"backgroundTable\">
                	<tr>
                    	<td align=\"center\" valign=\"top\">
                            <!-- // Begin Template Preheader \\ -->
                            <table border=\"0\" cellpadding=\"10\" cellspacing=\"0\" width=\"600\" id=\"templatePreheader\">
                                <tr>
                                    <td valign=\"top\" class=\"preheaderContent\">
                                    
                                    	<!-- // Begin Module: Standard Preheader \ -->
                                        <table border=\"0\" cellpadding=\"10\" cellspacing=\"0\" width=\"100%\">
                                        	<tr>
                                            	<td valign=\"top\">
                                                	<div>
                                                    	 
                                                    </div>
                                                </td>
                                                <!-- *|IFNOT:ARCHIVE_PAGE|* -->
    											<td valign=\"top\" width=\"170\">
                                                	<div>
                                                    	
                                                    </div>
                                                </td>
    											<!-- *|END:IF|* -->
                                            </tr>
                                        </table>
                                    	<!-- // End Module: Standard Preheader \ -->
                                        
                                    </td>
                                </tr>
                            </table>
                            <!-- // End Template Preheader \\ -->
                        	<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"600\" id=\"templateContainer\">
                            	<tr>
                                	<td align=\"center\" valign=\"top\">
                                        <!-- // Begin Template Header \\ -->
                                    	<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"600\" id=\"templateHeader\">
                                            <tr>
                                                <td class=\"headerContent\">
                                                
                                                    <!-- // Begin Module: Letterhead, Center Header Image \\ -->
                                                    <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">
                                                        <tr>
                                                        	<td class=\"leftHeaderContent\">
                                                                <div>
                                                                	<a href=\"www.cloudmunch.com\"><img alt=\"CloudMunch Logo\" src=\"http://www.cloudmunch.com/templates/cloudmunchtemplate3/img/cmLogo.gif\" border=\"0\" /></a>
                                                                </div>
                                                            </td>
                                                            <td valign=\"middle\" width=\"180\">
                                                               
                                                            </td>
                                                            <td class=\"rightHeaderContent\">
                                                                <div>
                                                                	
                                                                </div>
                                                            </td>
                                                        </tr>
                                                    </table>
                                                    <!-- // End Module: Letterhead, Center Header Image \\ -->
    
                                                </td>
                                            </tr>
                                        </table>
                                        <!-- // End Template Header \\ -->
                                    </td>
                                </tr>
                            	<tr>
                                	<td align=\"center\" valign=\"top\">
                                        <!-- // Begin Template Body \\ -->
                                    	<table border=\"0\" cellpadding=\"10\" cellspacing=\"0\" width=\"600\" id=\"templateBody\">
                                        	<tr>
                                            	<td valign=\"top\" class=\"bodyContent\">
                                                
                                                    <!-- // Begin Module: Standard Content \\ -->
                                                    <table border=\"0\" cellpadding=\"10\" cellspacing=\"0\" width=\"100%\">
                                                        <tr>
                                                            <td valign=\"top\">
                                                                <div><!-- //////////Add Content Here ////////////// -->
                                                                	
                                                                	<p>Hi ".$formvars['name'].",</p>
                                                                      <p>Thank you for your interest in CloudMunch. To sign-in, you will need to use your company email ID. Request you to please sign-in using your company email ID.</p>
                                                                    	<p>CloudMunch's All-in-One application delivery platform gives you all the tools you need to deliver great application with faster releases, better quality and lower costs to create a competitive advantage. Our goal is to enable you to focus on writing great code while CloudMunch takes care of the rest.</p>
                                                                    <p>Thanks,</p>
                                                                    <p><strong>The CloudMunch Team</strong></p>
    															</div><!-- //////////Content Ends Here ////////////// -->
    														</td>
                                                        </tr>
                                                    </table>
                                                    <!-- // End Module: Standard Content \\ -->
                                                
                                                </td>
                                            </tr>
                                        </table>
                                        <!-- // End Template Body \\ -->
                                    </td>
                                </tr>
                            	<tr>
                                	<td align=\"center\" valign=\"top\">
                                        <!-- // Begin Template Footer \\ -->
                                    	<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"600\" id=\"templateFooter\">
                                        	<tr>
                                            	<td valign=\"top\" class=\"footerContent\">
                                                <hr style=\"width:100%;\" />
                                                    <!-- // Begin Module: Standard Footer \\ -->
                                                    <table border=\"0\" cellpadding=\"10\" cellspacing=\"0\" width=\"100%\">
                                                        <tr>
                                                            <td valign=\"top\" width=\"350\">
                                                                <div>
    																<em>Copyright &copy; 2012 CloudMunch, All rights reserved.</em>
    														
    																</div>
                                                            </td>
                                                            <td valign=\"top\" width=\"190\" id=\"monkeyRewards\">
                                                                <div>
                                                                    You are recieving this email because you tried registering with CloudMunch. If you think you have recieved this email by error, please send a mail with your details to support@cloudmunch.com to unsubscribe.
    
                                                                
                                                            </td>
                                                        </tr>
    
                                                         </table>
                                                    <!-- // End Module: Standard Footer \\ -->
                                                
                                                </td>
                                            </tr>
                                        </table>
                                        <!-- // End Template Footer \\ -->
                                    </td>
                                </tr>
                            </table>
                            <br />
                        </td>
                    </tr>
                </table>
            </center>
        </body>
    </html>";

  //  $mailer->Body= "Hello ".$formvars['name'].",\r\n\r\n".
  //  "Thank you for your interest in Cloudmunch. To sign-in you will need to use your company email ID. Request you to pls sign-in using your company email ID.Cloudmunch's All-in-One software delivery platform gives you all the tools you need to deliver great software with faster releases, better quality and lower costs to create a competitive advantage. Our goal is to enable you to focus on writing great code while Cloudmunch takes care of the rest. \r\n\r\n".
  //  "Cloudmunch's All-in-One application delivery platform gives you all the tools you need to deliver great application with faster releases, better quality and lower costs to create a competitive advantage. Our goal is to enable you to focus on writing great code while Cloudmunch takes care of the rest. \r\n\r\n".
  //  "Best Regards,
  //  The Cloudmunch Team
  //  www.cloudmunch.com";

  if(!$mailer->Send()) {
   $this->HandleError("Failed sending registration confirmation email to ".$formvars['email']);
   return false;
  }
  return true;
 }

 function GetAbsoluteURLFolder() {
  //$scriptFolder=(isset($_SERVER['HTTPS']) &&($_SERVER['HTTPS'] == 'on')) ? 'https://' : 'http://';
  //$scriptFolder .= $_SERVER['HTTP_HOST'].dirname($_SERVER['REQUEST_URI']);
   
  //$scriptFolder = 'https://'.$_SERVER['HTTP_HOST'].dirname($_SERVER['REQUEST_URI']);
    $scriptFolder = 'https://'.$_SERVER['HTTP_HOST'];

  return $scriptFolder;
 }

 function SendAdminIntimationEmailAboutTrial($username, $useremail) {

  $file= $this->location.$this->usersTempListFileLoc;
   error_log(" in trial,file -  ".$file);
  if(!$this->FromSite()) {
   $file= $this->platformUserList;
  }
  $string= file_get_contents($file);
  $jsonContent= json_decode($string);

  $user= $jsonContent-> $useremail;
  $user->trialRequested= "true";
  $this->writeToUsersListFile($jsonContent, $file);
  error_log("write to userslist file done");
  if(empty($this->admin_email)) {
   return false;
  }
  $mailer= new PHPMailer();

  $mailer->CharSet= 'utf-8';

  $mailer->AddAddress($this->admin_email);

  $mailer->Subject= "CloudMunch Trial Requested by ".$username;

  $mailer->From= $this->GetFromAddress();
  $mailer->Body= "Following user requested a trial at ".$this->sitename."\r\n".
  "Name: ".$username."\r\n".
  "Email address: ".$useremail."\r\n";

  if(!$mailer->Send()) {
    error_log("sending mail failed");
   return false;
  }
      error_log("sending mail success");
  return true;
 }
 function SendAdminIntimationEmail(& $formvars) {
  if(empty($this->admin_email)) {
   return false;
  }
  $mailer= new PHPMailer();

  $mailer->CharSet= 'utf-8';

  $mailer->AddAddress($this->admin_email);

  $mailer->Subject= "New registration: ".$formvars['name'];

  $mailer->From= $this->GetFromAddress();

  $mailer->Body= "A new user registered at ".$this->sitename."\r\n".
  "Name: ".$formvars['name']."\r\n".
  "Email address: ".$formvars['email']."\r\n".
  "IP Address: ".$formvars['ip'];

  if(!$mailer->Send()) {
   return false;
  }
  return true;
 }
 function SendAdminIntimationEmailAboutUnKnown(& $formvars) {
  if(empty($this->admin_email)) {
   return false;
  }
  $mailer= new PHPMailer();

  $mailer->CharSet= 'utf-8';

  $mailer->AddAddress($this->admin_email);

  $mailer->Subject= "New unknown user: ".$formvars['email'];

  $mailer->From= $this->GetFromAddress();

  $mailer->Body= "A new unknown user tried to register at ".$this->sitename."\r\n".
  "Name: ".$formvars['name']."\r\n".
  "Email address: ".$formvars['email']."\r\n".
  "IP Address: ".$formvars['ip'];

  if(!$mailer->Send()) {
   return false;
  }
  return true;
 }

 function SaveToDatabase(& $formvars) {
  if(!$this->DBLogin()) {
   $this->HandleError("Database login failed!");
   return false;
  }
  if(!$this->Ensuretable()) {
   return false;
  }
  if(!$this->IsFieldUnique($formvars, 'email')) {
   $this->HandleError("This email is already registered");
   return false;
  }

  if(!$this->IsFieldUnique($formvars, 'username')) {
   $this->HandleError("This UserName is already used. Please try another username");
   return false;
  }
  if(!$this->InsertIntoDB($formvars)) {
   $this->HandleError("Inserting to Database failed!");
   return false;
  }
  return true;
 }

 function IsFieldUnique($formvars, $fieldname) {
  $field_val= $this->SanitizeForSQL($formvars[$fieldname]);
  $qry= "select username from $this->tablename where $fieldname='".$field_val."'";
  $result= mysql_query($qry, $this->connection);
  if($result && mysql_num_rows($result) > 0) {
   return false;
  }
  return true;
 }

 function DBLogin() {

  $this->connection= mysql_connect($this->db_host, $this->username, $this->pwd);

  if(!$this->connection) {
   $this->HandleDBError("Database Login failed! Please make sure that the DB login credentials provided are correct");
   return false;
  }
  if(!mysql_select_db($this->database, $this->connection)) {
   $this->HandleDBError('Failed to select database: '.$this->database.' Please make sure that the database name provided is correct');
   return false;
  }
  if(!mysql_query("SET NAMES 'UTF8'", $this->connection)) {
   $this->HandleDBError('Error setting utf8 encoding');
   return false;
  }
  return true;
 }

 function Ensuretable() {
  $result= mysql_query("SHOW COLUMNS FROM $this->tablename");
  if(!$result || mysql_num_rows($result) <= 0) {
   return $this->CreateTable();
  }
  return true;
 }

 function CreateTable() {
  $qry= "Create Table $this->tablename (".
  "id_user INT NOT NULL AUTO_INCREMENT ,".
  "name VARCHAR( 128 ) NOT NULL ,".
  "email VARCHAR( 64 ) NOT NULL ,".
  "phone_number VARCHAR( 16 ) NOT NULL ,".
  "username VARCHAR( 16 ) NOT NULL ,".
  "password VARCHAR( 32 ) NOT NULL ,".
  "confirmcode VARCHAR(32) ,".
  "PRIMARY KEY ( id_user )".
  ")";

  if(!mysql_query($qry, $this->connection)) {
   $this->HandleDBError("Error creating the table \nquery was\n $qry");
   return false;
  }
  return true;
 }

 function InsertIntoDB(& $formvars) {

  $confirmcode= $this->MakeConfirmationMd5($formvars['email']);

  $formvars['confirmcode']= $confirmcode;

  $insert_query= 'insert into '.$this->tablename.'(
                     																	name,
                     																	email,
                     																	username,
                     																	password,
                     																	confirmcode
                     																	)
                     																	values
                     																	(
                     																	"'.$this->SanitizeForSQL($formvars['name']).'",
                     																	"'.$this->SanitizeForSQL($formvars['email']).'",
                     																	"'.$this->SanitizeForSQL($formvars['username']).'",
                     																	"'.md5($formvars['password']).'",
                     																	"'.$confirmcode.'"
                     																	)';
  if(!mysql_query($insert_query, $this->connection)) {
   $this->HandleDBError("Error inserting data to the table\nquery:$insert_query");
   return false;
  }
  return true;
 }
 function MakeConfirmationMd5($email) {
  $randno1= rand();
  $randno2= rand();
  return md5($email.$this->rand_key.$randno1.''.$randno2);
 }
 function SanitizeForSQL($str) {
  if(function_exists("mysql_real_escape_string")) {
   $ret_str= mysql_real_escape_string($str);
  } else {
   $ret_str= addslashes($str);
  }
  return $ret_str;
 }

 /*
 Sanitize() function removes any potential threat from the
 
 data submitted. Prevents email injections or any other hacker attempts.
 if $remove_nl is true, newline chracters are removed from the input.
 */
 function Sanitize($str, $remove_nl= true) {
  $str= $this->StripSlashes($str);

  if($remove_nl) {
   $injections= array('/(\n+)/i', '/(\r+)/i', '/(\t+)/i', '/(%0A+)/i', '/(%0D+)/i', '/(%08+)/i', '/(%09+)/i');
   $str= preg_replace($injections, '', $str);
  }

  return $str;
 }
 function StripSlashes($str) {
  if(get_magic_quotes_gpc()) {
   $str= stripslashes($str);
  }
  return $str;
 }
}
?>																																																																																																																																																																																										                                                                                                                                                                                                                                                                                                                               