
var date  = new Date();
console.log(date.toDateString().substring(4,10)+","+date.toDateString().substring(11,15)+", "+date.toLocaleTimeString().substring(0,5)+" "+date.toLocaleTimeString().substring(8));
let socket = null;
let messageObject=[] ;
function appendTomsg(chatmsg){
	this.messageObject.push(chatmsg);
}
function getMessages(){
	var url = window.location.href+"userview";
	ajaxCall(url,'POST',"type=getMessages").then(function(result){
		let resJson = JSON.parse(result);
		let messages=[];
		for (let index = 0; index < resJson.length; index++) {
			const element = resJson[index];
			let chatObj = {
				sender :`${element.sender}`,
				message : `${element.message}`,
				receiver : `${element.receiver}`,
				date:`${element.date}`,
				time:`${element.time}`
			};
			// if(messageObject.has(chatObj.sender)){
			// 	let arr = messageObject.get(chatObj.sender);
			// 	arr[arr.length]=chatObj;
			// 	messageObject.set(chatObj.sender, arr);
			// }else{
			// 	messageObject.set(chatObj.sender,chatObj);
			// }
			addTochats(chatObj);
			messages[index]=chatObj;
		}
		this.messageObject=messages;
	})
}
function validFails(id,message){
    var a =document.getElementById(id);
    a.className='show';
    setTimeout(function() { a.className = a.className.replace("show", ""); }, 3000);
	launch_toast(message);
}



function login(login) {

	var url = window.location.href+"loginPage";
	
	var input = "type=" + login;
	if (login === 'signup') {

		var mobile = document.getElementById("mobilenumber");
        var username = document.getElementById("usernamesignup");
	    var password = document.getElementById("passwordsignup");
        if(!uservalid(username.value)){
            validFails("usernameValid","Enter valid Details...");
           
	    	return;
        }
        if (!phonenumber(mobile.value)) {
            validFails("mobileValid","Enter valid Details...");
			return;
		}
	    if (!passwordValid(password.value)) {
            validFails("passwordValid","Enter valid Details...");
            
	    	return;
	    }
		
		input = input +"&username=" + username.value + "&password=" + password.value+ "&mobilenumber=" + mobile.value;
		
	}
    else{
        var username = document.getElementById("username");
	    var password = document.getElementById("password");
        if(!uservalid(username.value)){
            validFails("usernameValidlog","Enter valid Details...");
            
	    	return;
        }
	    if (!passwordValid(password.value)) {
            validFails("passwordValidlog","Enter valid Details...");
           
	    	return;
	    }
        input = input +"&username=" + username.value + "&password=" + password.value
    }
    
	var xhr = new XMLHttpRequest();

	xhr.onreadystatechange = function() {
		if (xhr.readyState == 4 && xhr.status==200) {
			if (xhr.responseText === 'success') {
				headerSelect(document.getElementById('allHead'));
                userview();
				socket = createChatSocket(document.getElementById("userId").innerHTML);
				 
			} else {
				alert(xhr.responseText);
			}
			document.getElementById("mobilenumber").value="";
			document.getElementById("usernamesignup").value="";
			document.getElementById("passwordsignup").value="";
			document.getElementById("username").value="";
			document.getElementById("password").value="";
		}
	}
	xhr.open("POST", url, true);
	xhr.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
	xhr.send(input);

}

function updateChatSpace(sender,receiver){
	
	
	
	for (let index = 0; index < this.messageObject.length; index++) {
		let templateTag = document.getElementsByTagName("template")[3];
	let eachItemDiv = templateTag.content;
	let copiedDiv = eachItemDiv.cloneNode(true);
	let templateTagR = document.getElementsByTagName("template")[4];
	let eachItemDivR = templateTagR.content;
	let copiedDivR = eachItemDivR.cloneNode(true);
	
		const element = this.messageObject[index];
		if(element.sender===sender && element.receiver===receiver){
			copiedDiv.querySelectorAll("#sender")[0].children[0].children[0].innerText = element.message;
			copiedDiv.querySelectorAll("#sender")[0].children[0].children[1].innerText = element.time;
			document.getElementById("chatSpace").append(copiedDiv);
			
			// copiedDivR.querySelectorAll("#receiver")[0].innerText = element.message;
			// document.getElementById("chatSpace").append(copiedDivR);
			
		}
		if(element.sender===receiver && element.receiver===sender){
			copiedDivR.querySelectorAll("#receiver")[0].children[0].children[0].innerText = element.message;
			copiedDivR.querySelectorAll("#receiver")[0].children[0].children[1].innerText = element.time;
			document.getElementById("chatSpace").append(copiedDivR);
			// copiedDiv.querySelectorAll("#sender")[0].innerText = element.message;
			// document.getElementById("chatSpace").append(copiedDiv);
		}
	}
	if(document.getElementById("chatSpace").children.length>7){
		document.getElementById("chatSpace").scrollTop=document.getElementById("chatSpace").scrollHeight;
	}
}

function snedMessage(dom){
	let fromChat = dom;
	

	let button = dom;
	button = dom;
	let chatView = document.getElementById("chatView");
	
	const buttonRect = button.getBoundingClientRect();
	if(buttonRect.top>235){
		chatView.style.top = `35%`;
	}else{
  	chatView.style.top = `${buttonRect.top}px`;}
	  chatView.style.left='73%';
  	// chatView.style.right = `${buttonRect.left}px`;
  	chatView.style.display = "flex";



	dom = dom.closest('div');
	dom=dom.parentNode;
	dom.className='selected';
	var name = document.querySelector(".selected").children[0].children[1].innerText;
	var imgDom = document.querySelector(".selected").children[0].children[0].innerHTML;
	if(fromChat.id==='contactOnChat'){
		imgDom = document.querySelector(".selected").children[0].children[0].children[0].innerHTML;
	}
	if(imgDom===name.charAt(0)){
		imgDom=document.querySelector(".selected").children[0].children[0].cloneNode(true);
		chatView.children[0].children[0].children[0].replaceWith(imgDom);
		// document.querySelector(".selected").children[0].children[0].replaceWith(imgDom);
	}else{
		chatView.children[0].children[0].innerHTML=imgDom;
	}
	var mobileNumber = document.querySelector(".selected").children[1].innerText;
	
	if(fromChat.id==='contactOnChat'){
		name = fromChat.children[1].children[0].innerHTML;
		let list = document.getElementById("contactlist");
		mobileNumber = fromChat.children[1].children[2].innerHTML;
		// let contactFound = false;
		// for (let index = 1; index < list.children.length; index++) {
		// 	const element = array[index];
		// 	// if(element.)
		// }
		imgDom = fromChat.children[0].children[0].innerHTML;
		if(imgDom===name.charAt(0)){
			imgDom=fromChat.children[0].children[0].cloneNode(true);
			chatView.children[0].children[0].children[0].replaceWith(imgDom);
		}
		document.getElementById("chatIcon").click();
	}
	chatView.style.visibility='visible';
	
	chatView.children[0].children[1].innerText=name;
	updateChatSpace(document.getElementById('userId').innerHTML,mobileNumber);
	var message = document.getElementById("message");
	message.addEventListener("keyup",function(){
		var sendButton = document.getElementById("sendButton");
		if(message.value.trim()!=""){
			
			sendButton.style.visibility='visible';
			sendButton.addEventListener('click',function(event){
				let receiverId = mobileNumber;
				send(message,receiverId);
				let messageObj = {
					message:message.value,
					receiver :receiverId
				}
				addTochats(messageObj)
				message.value="";
				sendButton.style.visibility='hidden';
				event.stopImmediatePropagation();
				return;
			},{once:true})	
		}else{
			sendButton.style.visibility='hidden';
		}
		
	})
	listner(dom);
	//  	document.addEventListener("mousedown", function(event) {
	// 	if(document.getElementById("chatView").style.visibility==='hidden'){
	// 		return;
	// 	}
	// 	const target = event.target;
	// 	let call = true;
	// 	if (target !== sendButton && target !== chatView &&
	// 		target!==document.getElementById("chatSpace") &&
	// 		target!==document.getElementById("messageAndSend") &&
	// 		target!==document.getElementById("messageAndSend") &&
	// 		target!==document.getElementById("message")) {
	// 			if(document.getElementById("chatSpace").children.length>=1){
	// 				var srdom = document.getElementById("chatSpace");
	// 				var found = false;
	// 				for (let index = 0; index < srdom.children.length; index++) {
	// 					const element = srdom.children[index];
	// 					if(target===element.children[0]||target===element.children[0].children[0] ||target===element.children[0].children[1]||
	// 						target.innerHTML===element.innerHTML
	// 						){
	// 							found=true;
	// 							if(target===document.getElementById("chatHeader").children[0].children[0]){
	// 								viewContact(dom);
	// 								document.removeEventListener("mousedown",this);
	// 							}
	// 							dom.className='';
	// 							return;
	// 					}
	// 				}
	// 					if(found==false){
	// 						chatView.style.display = "none";
	// 							chatView.style.visibility='hidden';
	// 							while (chatView.children[1].firstChild) {
	// 								chatView.children[1].removeChild(chatView.children[1].firstChild);
	// 							}
								
	// 					}
	// 				}
				
	// 		chatView.style.display = "none";
	// 		chatView.style.visibility='hidden';
	// 		while (chatView.children[1].firstChild) {
	// 			chatView.children[1].removeChild(chatView.children[1].firstChild);
	// 		}
	// 		if(target===document.getElementById("chatHeader")||target===document.getElementById("chatHeader").children[0]|| target===document.getElementById("chatHeader").children[0].children[0]||target===document.getElementById("chatName")){
				
	// 			viewContact(dom);
	// 			document.removeEventListener("mousedown",this);
	// 		}
	// 	}else{

	// 	}
	//   },{once:true});
	dom.className='';
	return;
}


function listner(dom){
	document.addEventListener("mousedown", function(event) {
		if(document.getElementById("chatView").style.visibility==='hidden'){
			return;
		}
		const target = event.target;
		let call = true;
		if (target !== sendButton && target !== chatView &&
			target!==document.getElementById("chatSpace") &&
			target!==document.getElementById("messageAndSend") &&
			target!==document.getElementById("messageAndSend") &&
			target!==document.getElementById("message")) {
				if(document.getElementById("chatSpace").children.length>=1){
					var srdom = document.getElementById("chatSpace");
					var found = false;
					for (let index = 0; index < srdom.children.length; index++) {
						const element = srdom.children[index];
						if(target===element.children[0]||target===element.children[0].children[0] ||target===element.children[0].children[1]||
							target.innerHTML===element.innerHTML
							){
								found=true;
								if(target===document.getElementById("chatHeader").children[0].children[0]){
									viewContact(dom);
									document.removeEventListener("mousedown",this);
								}
								dom.className='';
								listner(dom);
								return;
						}
					}
						if(found==false){
							chatView.style.display = "none";
								chatView.style.visibility='hidden';
								while (chatView.children[1].firstChild) {
									chatView.children[1].removeChild(chatView.children[1].firstChild);
								}
								
						}
					}
				
			chatView.style.display = "none";
			chatView.style.visibility='hidden';
			while (chatView.children[1].firstChild) {
				chatView.children[1].removeChild(chatView.children[1].firstChild);
			}
			if(target===document.getElementById("chatHeader")||target===document.getElementById("chatHeader").children[0]|| target===document.getElementById("chatHeader").children[0].children[0]||target===document.getElementById("chatName")){
				
				viewContact(dom);
				document.removeEventListener("mousedown",this);
			}
		}else{
			listner(dom);
		}
	  },{once:true});
}

function notification(from){
	if('Notification' in window && Notification.permission==="granted"){
		new Notification("New Message",{"new message from":from});
	}else if(Notification.permission==="default"){
		Notification.requestPermission().then(permission=>{if(permission==="granted"){
			new Notification("New Message",{"new message from":from});
		}});
	}
}
function send(message,receiverId){
	
	var date  = new Date();
	// console.log(date.toDateString().substring(4,10)+","+date.toDateString().substring(11,15)+", "+date.toLocaleTimeString().substring(0,4));
	var datestr = date.toDateString().substring(4,10)+","+date.toDateString().substring(11,15);
	var time = date.toLocaleTimeString().substring(0,5)+" "+date.toLocaleTimeString().substring(8);
	socket.send(message.value+','+document.getElementById('userId').innerHTML+","+receiverId+","+datestr+" "+time);
	let chatMsg={
				sender :`${document.getElementById('userId').innerHTML}`,
				message : `${message.value}`,
				receiver : `${receiverId}`,
				date:`${date}`,
				time:`${time}`
	};
	appendTomsg(chatMsg);
	let templateTag = document.getElementsByTagName("template")[3];
			let eachItemDiv = templateTag.content;
			let copiedDiv = eachItemDiv.cloneNode(true);
			copiedDiv.querySelectorAll("#sender")[0].children[0].children[0].innerText = message.value;
			copiedDiv.querySelectorAll("#sender")[0].children[0].children[1].innerText = time;
			document.getElementById("chatSpace").append(copiedDiv);
			if(document.getElementById("chatSpace").children.length>10){
			document.getElementById("chatSpace").scrollTop=document.getElementById("chatSpace").scrollHeight;
		}
}
function createChatSocket(userId){
	socket = new WebSocket("ws://"+ window.location.host + window.location.pathname + "chat/"+userId);
	// socket.onopen = function(event) {
    //     socket.send(userId);
    // };

    socket.onmessage = function(event) {

		var message = JSON.parse(event.data);
		addTochats(message);
		let chatmsg = {
			sender :`${message.sender}`,
				message : `${message.message}`,
				receiver : `${document.getElementById('userId').innerHTML}`,
				date:`${message.date}`,
				time:`${message.time}`
		}
		appendTomsg(chatmsg);
		let contects = document.getElementById("contactlist");
		var name='';
		for (let index = 1; index < contects.children.length; index++) {
			const element = contects.children[index];
			if(element.children[1].innerText===message.sender){
				name=element.children[0].children[1].innerHTML;
			}
		}
		if(document.getElementById("chatView").style.display==='flex' && 
		
		document.getElementById("chatHeader").children[1].innerHTML===name){
			let templateTag = document.getElementsByTagName("template")[4];
			let eachItemDiv = templateTag.content;
			let copiedDiv = eachItemDiv.cloneNode(true);
			copiedDiv.querySelectorAll("#receiver")[0].children[0].children[0].innerText = message.message;
			copiedDiv.querySelectorAll("#receiver")[0].children[0].children[1].innerText = message.time;
			document.getElementById("chatSpace").append(copiedDiv);
			if(document.getElementById("chatSpace").children.length>10){
				document.getElementById("chatSpace").scrollTop=document.getElementById("chatSpace").scrollHeight;
		}
		}
		notification(message.sender);
		// alert(`From :${message.userId} Message :${message.message}`);
    };

    socket.onclose = function(event) {
        console.log("WebSocket connection closed.");
    };

    socket.onerror = function(event) {
        console.log("WebSocket error: " + event);
    };

    return socket;

}

function addTochats(message){
	var cont = document.getElementById("contactlist");
		var added = false;
		var sender ='';
		var imgDom='';
		var contactFound =false;
		for(let index = 1; index < cont.children.length; index++) {
			const element = cont.children[index];
			if(element.children[1].innerText===message.sender||element.children[1].innerText===message.receiver){
				sender = element.children[0].children[1].innerText;
				imgDom = element.children[0].children[0].cloneNode(true);
				contactFound = true;
				break;
			}
		}
		let chatDiv = document.getElementById("chat");
		for(let index1=0;index1<chatDiv.children.length;index1++){
			let element = chatDiv.children[index1];
			if(element.children[1].children[0].innerHTML===sender){
				added=true;
				element.children[1].children[1].innerHTML=message.message;
				return;
			}
		}
		if(added==false){
			let templateTag = document.getElementsByTagName("template")[2];
			let eachItemDiv = templateTag.content;
			let copiedDiv = eachItemDiv.cloneNode(true);
			
			copiedDiv.querySelectorAll("#contactOnChat")[0].children[0].append(imgDom);
			if(imgDom.children.length==0){
				copiedDiv.querySelectorAll("#contactOnChat")[0].children[0].children[0].style.marginLeft='17px';
				copiedDiv.querySelectorAll("#contactOnChat")[0].children[0].children[0].style.width='45px';
			}
			copiedDiv.querySelectorAll("#contactOnChat")[0].children[1].children[0].innerHTML=sender;
			copiedDiv.querySelectorAll("#contactOnChat")[0].children[1].children[1].innerHTML = message.message;
			copiedDiv.querySelectorAll("#contactOnChat")[0].children[1].children[2].innerHTML = message.sender;
			if(message.sender==document.getElementById('userId').innerHTML){
				copiedDiv.querySelectorAll("#contactOnChat")[0].children[1].children[2].innerHTML = message.receiver;
			}
			
			document.getElementById("chat").append(copiedDiv);
		}
}
function uservalid(inputtxt) {
	var userParam = /^[A-Za-z][A-Za-z0-9_]{5,29}$/;
	if (inputtxt.match(userParam)) {
		return true;
	}
	return false;
}
function passwordValid(inputtxt) {
	var passwordParam = /(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,}/;
	if (inputtxt.match(passwordParam)) {
		return true;
	}
	return false;
}
function phonenumber(inputtxt) {
	var phoneno = /^\d{10}$/;
	if (inputtxt.match(phoneno)) {
		return true;
	}
	return false;
}
function chechLogin(){
    let url = window.location.href+"userview";
    var input = "type=loginCheck";
    ajaxCall(url,"POST",input).then(function(result){
        if(result.trim()==='alredayLogin'){
            userview();
			
		}
    })
}

function loadHandlers(type) {

	let url = window.location.href+"userview";
	var input = "type=" + type;
	if (type === "search") {
		if(document.getElementById('searchText').value===''){
			loadHandlers('normal');
		}
		input = input + "&key=" + document.getElementById("searchText").value;
	}
	ajaxCall(url, "POST", input).then(function(result) {
		var userJason = JSON.parse(result);
		if (userJason.message !== undefined || userJason.message === "Please Login") {
			alert("Please login")
			document.getElementById("usermain").style.display='none';
            document.getElementById("signuppage").style.display='flex';
			return;
		}
		document.getElementById("contactlist").innerHTML = "<div id='ContHead'>All contacts</div>";
		document.getElementById("favcontactlist").innerHTML="<div id='favContHead'>Starred contacts</div>";
		document.getElementById("userNameh2").innerHTML = userJason[0].name;
		document.getElementById("userId").innerHTML = userJason[0].mobilenumber;
        if(userJason[1]==null){
            document.getElementById('NoContacts').className='show';

        }else{
			document.getElementById('NoContacts').className='';
		}
		for (let i = 1; i < userJason.length && userJason[1]!== null; i++) {
			let contact = userJason[i];
			let templateTag = document.getElementsByTagName("template")[0];
			let eachItemDiv = templateTag.content;
			let copiedDiv = eachItemDiv.cloneNode(true);
			copiedDiv.querySelectorAll(".contactname")[0].innerText = contact.name;
			copiedDiv.querySelectorAll(".contactnumber")[0].innerText = contact.number;
			if(contact.image!==''){
				copiedDiv.querySelectorAll(".imageDiv")[0].innerHTML=contact.image;
			}else{
				var randomColor = Math.floor(Math.random()*16777215).toString(16);
				while(randomColor==00000000){
					 randomColor = Math.floor(Math.random()*16777215).toString(16);
				}
				copiedDiv.querySelectorAll(".imageDiv")[0].style.backgroundColor = "#" + randomColor;
				copiedDiv.querySelectorAll(".imageDiv")[0].innerHTML = contact.name.charAt(0);
			}
			if(contact.favourite===true){
				let contact = userJason[i];
				let favTemplateTag = document.getElementsByTagName("template")[1];
				let faveachItemDiv = favTemplateTag.content;
				let favcopiedDiv = faveachItemDiv.cloneNode(true);
				favcopiedDiv.querySelectorAll(".contactname")[0].innerText = contact.name;
				favcopiedDiv.querySelectorAll(".contactnumber")[0].innerText = contact.number;
				favcopiedDiv.querySelectorAll(".imageDiv")[0].innerHTML=copiedDiv.querySelectorAll(".imageDiv")[0].innerHTML;
				copiedDiv.children[0].children[2].children[0].style.color='black';
				favcopiedDiv.children[0].children[2].children[0].style.color='black';
				document.getElementById('favcontactlist').append(favcopiedDiv);
				
			}else{
				copiedDiv.children[0].children[2].children[0].style.color='white';
			}
			
			document.getElementById("contactlist").append(copiedDiv);
			viewAll(document.getElementById("allHead"));
			
		}
		if(type==='normal'){
			socket = createChatSocket(document.getElementById("userId").innerHTML);
		}
		getMessages();
	});
	
}

function viewContact(x){
	x.className='selected';
	document.getElementById("contactView").style.display='flex';
	document.getElementById("contactlist").style.display='none';
	document.getElementById("favcontactlist").style.display='none';
	// document.getElementById("headings").style.display='none';
	
	var contName = document.querySelector(".selected").children[0].children[1].innerHTML;
    var contNumber = document.querySelector(".selected").children[1].children[0].innerHTML;
	var image = document.querySelector(".selected").children[0].children[0].innerHTML;
	document.getElementById("contactViewName").innerHTML = contName;
	document.getElementById("contactViewNumber").innerHTML = contNumber;
	document.getElementById("contactImage").innerHTML = image;
	if(document.querySelector(".selected").children[0].children[0].children[0]===undefined){
	document.getElementById("contactImage").style.backgroundColor=document.querySelector(".selected").children[0].children[0].style.backgroundColor;}
	document.getElementById("conView").children[3].innerHTML=document.querySelector('.selected').children[2].innerHTML;
	x.className=x.className.replace("selected","");
}
function closeContact(){
	document.getElementById("contactViewName").innerHTML = '';
	document.getElementById("contactViewNumber").innerHTML = '';
	document.getElementById("contactImage").style.background='transparent';
	document.getElementById("contactImage").innerHTML = '';
	viewAll(document.getElementById('allHead'));
}
function viewFavs(dom){
	headerSelect(dom);
	document.getElementById("contactlist").style.display='none';
	document.getElementById("editWindw").style.display='none';
	document.getElementById("contactView").style.display='none';
	document.getElementById("favcontactlist").style.display='flex';
	if(document.getElementById('favcontactlist').childNodes.length===1){
		document.getElementById('NoContacts').className='show'
	}else{
		document.getElementById('NoContacts').className=document.getElementById('NoContacts').className.replace('show','');
	}
}
function viewAll(dom){
	headerSelect(dom);
	document.getElementById("contactlist").style.display='flex';
	document.getElementById("favcontactlist").style.display='none';
	document.getElementById("contactView").style.display='none';
	document.getElementById("editWindw").style.display='none';
	if(document.getElementById('contactlist').childNodes.length===1){
		document.getElementById('NoContacts').className='show'
	}else{
		document.getElementById('NoContacts').className=document.getElementById('NoContacts').className.replace('show','');
	}
}
function launch_toast(condition) {
    document.getElementById("desc").innerText=condition;
	var x = document.getElementById("invalidDetails");
	x.className = "show";
	setTimeout(function() { x.className = x.className.replace("show", ""); }, 3000);
}

function logout(dom) {
	headerSelect(dom);
	var url = window.location.href+"loginPage?type=logout";
	ajaxCall(url, "POST", "type=logout").then(function() {
		alert("Logout Success");
        document.getElementById("signuppage").style.display='flex';
        document.getElementById("loginpage").style.display='none';
        document.getElementById("usermain").style.display='none';
		
	})
}
function ajaxCall(url, method, input) {
	var promise = new Promise(function(resolve, reject) {
		var xhr = new XMLHttpRequest();

		xhr.onreadystatechange = function() {
			if (xhr.readyState == 4 && xhr.status == 200) {
				resolve(xhr.responseText);
			}
		}
		xhr.open(method, url, true);
		xhr.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
		xhr.send(input);
	});
	return promise;
}


function getContact(x){
        x.className='selected';
        var name = document.querySelector(".selected").children[0].children[1].innerText;
        var number = document.querySelector(".selected").children[1].innerText;
		
        document.getElementById("yes").addEventListener('click',function(){
            ajaxCall(window.location.href+"userview","POST","type=removeContact&contactnamein="+name+"&contactnumberin="+number).then(function(result){
                if(result.trim()==='success'){
                    launch_toast("Contact Deleted..");
                    cancelDelte();
                    loadHandlers('normal');
                }
            })
        })
        x.className=x.className.replace("selected","");
       
}
function insertContact(type) {
	var NoContactsPage = document.getElementById('NoContacts');
	NoContactsPage.className =  NoContactsPage.className.replace('show','');
	var button = document.getElementById("contactsAddbutton");
	document.getElementById("closingButton").addEventListener("click",function(){
		document.getElementById('addHead').className=document.getElementById('addHead').className.replace('selectedHead','');
		return;
	})
	button.addEventListener("click", function() {
		var cName = document.getElementById("contactnamein").value;
		var cNumber = document.getElementById("contactnumberin").value;
		var img = document.getElementById("imgIn").files[0];
		var valid = validateInput(cName, cNumber);
		var input = "type=" + type + "&contactnamein=" + cName + "&contactnumberin=" + cNumber;
		var fileInput = document.getElementById('imgIn');
		var file = null;
		var formData = new FormData();
		
		if(img!=null||img!=undefined){
			file = fileInput.files[0];
			formData.append('image', file);
			formData.append('contactnumberin',cNumber);
			formData.append('type','addImage');
		}
        if(!phonenumber(cNumber)){
            launch_toast("Enter valid Contact Details..");
            return;
        }
		if (!valid) {
			alert("Please enter valid Input");
			return;
		}
		
		ajaxCall(window.location.href+"userview", "POST", input).then(
			function(result) {
				if(img!=null||img!=undefined && result==="Contact Added Success"){
				var xhr = new XMLHttpRequest();

				xhr.onreadystatechange = function() {
					if (xhr.readyState == 4 && xhr.status == 200) {
						launch_toast(xhr.responseText);
						loadHandlers('normal');
					}	
				}
				xhr.open("POST", window.location.href+"userview?type=addImage", true);
				xhr.setRequestHeader("enctype", "multipart/form-data");
				xhr.send(formData);
				}
				else{
				launch_toast(result);
				loadHandlers('normal');
				}
				document.getElementById("contactnamein").value="";
				document.getElementById("contactnumberin").value="";
				
			}
		)
	});
	
}

function contactview(dom){
	viewContact(dom.closest('div'));
}
function addTofav(contact){
	contact =  contact.parentNode.parentNode;
	contact =  contact.closest('div');
	contact.className='selected';
	if(document.getElementById('contactView').style.visibility==='visible'){
		var cName = document.querySelector(".selected").children[2].children[0].innerText;
		var cNumber = document.querySelector(".selected").children[2].children[1].innerText;
	}
	else{
	contact =  contact.closest('div');
	contact.className='selected';
         cName = document.querySelector(".selected").children[0].innerText;
         cNumber = document.querySelector(".selected").children[1].innerText;
	}
		var input = "type=addToFavs&contactnamein=" + cName + "&contactnumberin=" + cNumber;
			ajaxCall(window.location.href+"userview","POST",input).then(function(result){
				launch_toast(result);
				loadHandlers('normal');
			});
	contact.className = contact.className.replace("selected","");
}

function validateInput(inputA, inputB) {
	if (inputA.trim() === "" || inputA === undefined) {
		return false;
	}
	if (inputB.trim() === "" || inputB === undefined) {
		return false;
	}
	return true;
}


function menubar(){
	var menuBar = document.getElementById("menubar");
	if(menuBar.style.display==='none'){
		document.getElementById("contactlist").style.width = '90%';
		document.getElementById("favcontactlist").style.width = '90%';
		menuBar.style.display='flex';
		document.getElementById("menu").style.background='transparent';
	}else{
		document.getElementById("menu").style.background="rgba(255, 255, 255, 0.25)";
		menuBar.style.display='none';
		document.getElementById("favcontactlist").style.width ='100%';
		document.getElementById("contactlist").style.width = '100%';
		
	}
    
}


function addContact(dom){
	headerSelect(dom);
    document.getElementById("show").click();
}



function cancelDelte(){
    document.getElementById("delContact").style.display='none';
}



function loginpage(){
    document.getElementById("signuppage").style.display='none';
    document.getElementById("loginpage").style.display='flex';
}
function userview(){
    document.getElementById("signuppage").style.display='none';
    document.getElementById("loginpage").style.display='none';
    document.getElementById("usermain").style.display='flex';
    loadHandlers('normal');
}
function getFile(){
	document.getElementById("imgIn").click();
	
}

function headerSelect(dom){
	var head1  = document.getElementById("allHead");
	var head2  = document.getElementById("favHead");
	var head3  = document.getElementById("addHead");
	var head4  = document.getElementById("logoutHead");
	if(head1.className==='selectedHead'){
		head1.className=head1.className.replace('selectedHead','');
	}else if(head2.className==='selectedHead'){
		head2.className=head2.className.replace('selectedHead','');
	}else if(head3.className==='selectedHead'){
		head3.className=head3.className.replace('selectedHead','');
		document.getElementById("show").click();
	}else if(head4.className==='selectedHead'){
		head4.className=head4.className.replace('selectedHead','');
	}
	dom.className='selectedHead';

}

function loadSearch(){
	document.getElementById("searchText").addEventListener('keyup',function(){
		loadHandlers('search');
	})
}

function printDiv(dom) {
	
	var divContents = dom;
	var a = window.open('', '', 'height=500, width=500');
	a.document.write('<html>');
	a.document.write('<body >');
	a.document.write(`<div style="height:400px;	width:300px;">${divContents.children[0].children[0].innerHTML}</div>`);
	a.document.write(`<h1>${divContents.children[0].children[1].innerHTML}</h1>`);
	a.document.write(`<h3>${divContents.children[1].innerHTML}</h3>`)
	a.document.write('</body></html>');
	a.document.close();
	a.print();
}
function viewOption(dom){
	button = dom;
	dom=document.getElementById("option");
	
	const buttonRect = button.getBoundingClientRect();
  dom.style.top = `${buttonRect.top}px`;
  dom.style.left = `${buttonRect.right}px`;
  dom.style.display = "block";
  document.getElementById("delete").addEventListener('click',function(){
	document.getElementById("delContact").style.display='flex';
	let element = button.parentNode;
	element= element.parentNode;
	getContact(element);
  });

  document.getElementById("print").addEventListener('click',function(){
	
	let element = button.parentNode;
	element= element.parentNode;
	printDiv(element);
  });

  document.getElementById("hide").addEventListener('click',function(){
	document.getElementById("delContact").style.display='flex';
	let element = button.parentNode;
	element= element.parentNode;
	hide(element);
  });
  document.addEventListener("click", function(event) {
	const target = event.target;
	if (target !== button && target !== dom && target !== document.getElementById("contactlist")
	&& target != document.getElementById("contacts")) {
		
	  dom.style.display = "none";
	  return;
	}
  });
  
}

function viewChat(){
	if(document.getElementById("chat").style.display==='flex'){
		document.getElementById("chat").style.display='none';
	}else{
		document.getElementById("chat").style.display='flex'
	}
}

function checkOption(){
	if(document.getElementById("option").style.display==='block'){
		document.getElementById("option").style.display='none';
	}
}

function clickimg(){
	document.getElementById("userImagein").click();
}
function closeEdit(){
	document.getElementById("contactView").style.display='flex';
	document.getElementById("editWindw").style.display='none';
}
function edit(dom){
	dom = dom.parentNode;
	dom = dom.parentNode;
	let name  = dom.children[2].children[0].innerHTML;
	let mobile = dom.children[2].children[1].innerHTML;
	
	document.getElementById("contactView").style.display='none';
	document.getElementById("editWindw").style.display='flex';

	document.getElementById("nameInEdit").innerText=name;
	document.getElementById("nameEdit").children[1].value=name;
	document.getElementById("nameEdit").click();
	document.getElementById("numberEdit").children[1].value=mobile;

	
	document.getElementById("editImage").addEventListener('click',function(){
		document.getElementById("imageEdit").click();
		document.getElementById("saveButton").style.cursor='pointer';
	})
	document.getElementById("nameEdit").children[1].addEventListener('keyup',function(){
		if(document.getElementById("nameEdit").children[1].valid!==name){
			document.getElementById("saveButton").style.cursor='pointer';
		}else{
			document.getElementById("saveButton").style.cursor='not-allowed'
			
		}
	})

	document.getElementById("numberEdit").children[1].addEventListener('keyup',function(){
		if(document.getElementById("numberEdit").children[1].valid!==mobile){
			document.getElementById("saveButton").style.cursor='pointer';
		}else{
			document.getElementById("saveButton").style.cursor='not-allowed'
		}
	})


	document.getElementById("saveButton").addEventListener('click',function(){
		if(document.getElementById("saveButton").style.cursor==='pointer'){
			let edname = 	document.getElementById("nameEdit").children[1].value;
			let edmobile = document.getElementById("numberEdit").children[1].value;
			let img = document.getElementById("imageEdit").files[0];
			let form = new FormData();
			if(img!=null || img != undefined){
				form.append('image',img);
				var xhr = new XMLHttpRequest();

				xhr.onreadystatechange = function() {
					if (xhr.readyState == 4 && xhr.status == 200) {
						launch_toast(xhr.responseText);
						loadHandlers('normal');
					}	
				}
				xhr.open("POST", window.location.href+"userview?type=editWithImage&contactnamein="+edname+"&contactnumberin="+edmobile+"&oldNum="+mobile, true);
				xhr.setRequestHeader("enctype", "multipart/form-data");
				xhr.send(form);

			}
			else{
				ajaxCall(window.location.href+"userview",'POST',"type=editWithoutImage&contactnamein="+edname+"&contactnumberin="+edmobile+"&oldNum="+mobile).then(function(result){
					launch_toast(result);
					loadHandlers('normal');
				})
			}

		}
	})


}