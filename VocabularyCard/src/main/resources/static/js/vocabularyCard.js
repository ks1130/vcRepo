'use strict';

//スクロール位置を保持する
function keepPosition(){
	var xpos=document.documentElement.scrollLeft||document.body.scrollLeft;
	var ypos=document.documentElement.scrollTop||document.body.scrollTop;
	sessionStorage.setItem("xpos",xpos);
	sessionStorage.setItem("ypos",ypos);
}
function resetPosition(){
	sessionStorage.setItem("xpos",0.0);
	sessionStorage.setItem("ypos",0.0);
}
if(sessionStorage.getItem("xpos")==null){
	resetPosition();
}
window.onload=function(){
		window.scrollTo(sessionStorage.getItem("xpos"),sessionStorage.getItem("ypos"));
}

//位置を保持/operation,/create,/createTag,/delete,/id,/addTag
//addTagのみクリックイベントではないので、個別に設定
$(".keep").on("click",function(){
	keepPosition();
});
//位置をリセット/search
$(".reset").on("click",function(){
	resetPosition;
});

//カード番号を表示
const card=document.getElementsByClassName("card");
const number=document.querySelectorAll(".check-delete>p>span");
for(i=0;i<card.length;i++){
	number[i].textContent=i+1;
}

//ドラッグアンドドロップ機能
const tag=document.querySelectorAll(".tagList>.tag");
const wordList=document.getElementsByClassName("wordList");
const idList=document.getElementsByClassName("id");
const form=document.getElementById("addTag");
if(screen.width>480){
	let draggedTag;
	let clickedTag;
	//全タグボタンにドラッグイベントとクリックイベントを設定、タグ名を取得
	for(let i=0;i<tag.length;i++){
		tag[i].addEventListener('dragstart',()=>{
			draggedTag=tag[i].textContent;
		});
		tag[i].addEventListener('click',()=>{
			//event.preventDefault();
			clickedTag=tag[i].textContent;
			//タグ検索フォームに代入
			document.getElementById('tagValue').value=clickedTag;
		});
		tag[i].addEventListener("drag",(event)=>{
			console.log("drag");
			event.preventDefault();
		});
	}
	let cardId;
	for(let i=0;i<wordList.length;i++){
	wordList[i].addEventListener("dragover",(event)=>{
		event.preventDefault();
	});
	wordList[i].addEventListener("drop",(event)=>{
		event.preventDefault();
		cardId=idList[i].textContent;

	document.getElementById("addedCard").value=cardId;
	document.getElementById("addedTag").value=draggedTag;
	//スクロール位置の保持
	keepPosition();
	form.submit();
	});
	}
}else{
	//スマホ用のドラッグアンドドロップ
	let X;
    let Y;
    let cardId;
    var wordListArray=[].slice.call(wordList);
    var movedTag=document.getElementById("movedTag");

	function touchStartEvent(event){
		event.preventDefault();
		var touchObject = event.changedTouches[0];
        X=touchObject.clientX;
        Y=touchObject.clientY;
        movedTag.style.position="fixed";
        movedTag.style.left=X+"px";
        movedTag.style.top=Y+"px";
		var touchObject = event.changedTouches[0];
        movedTag.textContent=event.target.textContent;
        movedTag.classList.toggle("displayNone");
        document.getElementById("addedTag").value=event.target.textContent;
        $(".menu-container").addClass("opacity");
        $(".float-container").css("zIndex","-10");
	}
	function touchMoveEvent(event){
		event.preventDefault();
		var touchObject = event.changedTouches[0];
        X=touchObject.clientX;
        Y=touchObject.clientY;
        movedTag.style.left=X+"px";
        movedTag.style.top=Y+"px";
        console.log("X:"+X+",Y:"+Y);
        var droppedCard=document.elementFromPoint(X,Y);
    	console.log(droppedCard.className);
	}
	function touchEndEvent(event){
		try{
			event.preventDefault();
			movedTag.style.position="";
        	//elementfromPoint()は最もネストされた要素を取得するため、その親要素がwordListかを判定する
        	var droppedElement=document.elementFromPoint(X,Y);
        	if(droppedElement.className=="card"){

        	}else{
        		var droppedCard=droppedElement.closest(".wordList");
        		if(droppedCard.className=="wordList"){
	          		var cardNumber=wordListArray.indexOf(droppedCard);
	          		cardId=idList[cardNumber].textContent;
	          		document.getElementById("addedCard").value=cardId;
	          		//スクロール位置の保持
	      	  		keepPosition();
	          		form.submit();
	        	}else{
	          		cardId=-1;
	          		console.log("false"+cardId);
	        	}
			}
      		}catch(error){
      			console.log("error");
			}finally{
				$(".menu-container").removeClass("opacity");

				$(".float-container").css("zIndex","100");
				movedTag.style.left=X+"";
        		movedTag.style.top=Y+"";
        		movedTag.classList.toggle("displayNone");
			}
}
for(var i=0;i<tag.length;i++){
   	tag[i].addEventListener("touchstart",touchStartEvent);
    tag[i].addEventListener("touchmove",touchMoveEvent);
    tag[i].addEventListener("touchend",touchEndEvent);
}
for(var i=0;i<tag.length;i++){
	tag[i].addEventListener("touchstart",touchStartEvent,false);
	tag[i].addEventListener("touchmove",touchMoveEvent,false);
	tag[i].addEventListener("touchend",touchEndEvent,false);
}
}

//ページネイション機能
//cardを配列として取得
const cardList=document.getElementsByClassName('card');
var array=Array.prototype.slice.call(cardList);
if(array.length!=0){
	const navi=document.querySelector(".navi");
	//全体を表示件数で割り、商の数だけページ番号を作成。
	for(let i=1;i<array.length/50+1;i++){
		const liElement=document.createElement('li');
		liElement.innerHTML='<li class="pageNumber pointer">'+i+'</li>';
		navi.insertAdjacentElement('beforeend',liElement);
	}
	//追加した<li>タグの個数分の配列を作成。
	let pageNumber=document.getElementsByClassName('pageNumber');
	var arrayPage=Array.prototype.slice.call(pageNumber);
	//1ページ目を最初に表示し、pointerをoffにする。currentPage==arrayPage[currentPage-1].textContent
	let currentPage=1;
	arrayPage[0].classList.toggle("pointer");
	//カード50個にtoggleを行うメソッドを定義。
	var displayCard=function(currentPage,array){
		for(let i=(currentPage-1)*50;i<((currentPage-1)*50)+50;i++){
		if(i+1>array.length)break;
		array[i].classList.toggle('displayByPage');
	}};
	displayCard(currentPage,array);
	//各ページ番号にクリック時のイベントを割り当てる。arrayPage[0]=1であることに注意
	for(let i=0; i<arrayPage.length;i++){
		arrayPage[i].addEventListener("click",()=>{
			if(currentPage==arrayPage[i].textContent){
				return;
			}else{
				//クリック前のページを非表示にし、pointerを付与
				displayCard(currentPage,array);
				arrayPage[currentPage-1].classList.toggle("pointer");
				//クリックしたページ番号に更新
				currentPage=(arrayPage[i].textContent);
				//更新後のページを表示し、pointerを削除
				displayCard(currentPage,array);
				arrayPage[currentPage-1].classList.toggle("pointer");
				window.scroll(0,0);
				}
});
}
}

//input入力内容があった場合にのみボタンをクリックできるようにする
function enableClick(button,inputArray){
	button.disabled=true;
	for(var i=0;i<inputArray.length;i++){
		inputArray[i].addEventListener('input',()=>{
			for (var j=0;j<inputArray.length;j++){
				if(inputArray[j].value.trim()!=null&&inputArray[j].value.trim()!==""){
				button.disabled=false;
				break;
				}else{
					button.disabled=true;
				}
			}
		});
	}
}
//各formに設定
var searchForm=document.getElementsByClassName('searchForm');
var searchButton=document.getElementById('doSearch');
enableClick(searchButton,searchForm);
var createForm=document.getElementsByClassName('createForm');
var createButton=document.getElementById('doCreate');
enableClick(createButton,createForm);
var createTagForm=document.getElementsByClassName('createTagForm');
var createTagButton=document.getElementById('doCreateTag');
enableClick(createTagButton,createTagForm);

//チェック時に削除ボタンを押せるようにする
const checkboxArray=document.getElementsByClassName("checkbox");
let deleteButton=document.getElementById('deleteButton');
for(var i=0;i<checkboxArray.length;i++){
	deleteButton.disabled=true;
	checkboxArray[i].addEventListener('change',()=>{
		for(var j=0;j<checkboxArray.length;j++){
		if(checkboxArray[j].checked==true){
			deleteButton.disabled=false;
			break;
		}else{
			deleteButton.disabled=true;
		}
		}
	});
}

//カード作成フォームをエンターキーでフォーカス移動するように設定
for(let i=0;i<createForm.length;i++){
	createForm[i].addEventListener("keypress",(event)=>{
		if(event.keyCode===13){
			event.preventDefault();
			if(i+1<createForm.length){
			createForm[i+1].focus();
			}
		}
	});
}
//カード追加ボタンのエンターキーでの送信を無効にする

//spellingTest機能
var L1flash=document.getElementById("L1flash");
var L2flash=document.getElementById("L2flash");
var L1hidden=document.querySelectorAll(".L1hidden");
var L2hidden=document.querySelectorAll(".L2hidden");
var L1writeIn=document.querySelectorAll(".L1writeIn");
var L2writeIn=document.querySelectorAll(".L2writeIn");
var L1Length=document.querySelectorAll(".L1Length");
var L2Length=document.querySelectorAll(".L2Length");
//正解の単語の文字数を表示
for(let i=0;i<L1hidden.length;i++){
	L1Length[i].textContent="(" + L1hidden[i].textContent.length + ")";
}
for(let i=0;i<L2hidden.length;i++){
	L2Length[i].textContent="(" + L2hidden[i].textContent.length + ")";
}
//回答⇔一覧の切り替えを設定
//表示=0,非表示=1
let currentL1Switch=0;
let currentL2Switch=0;
$("#L1flash").on("click",function(){
	if(currentL1Switch==0){
		for(let i=0;i<L1hidden.length;i++){
			if(!L1hidden[i].classList.contains('displayNone')){
				L1hidden[i].classList.toggle('displayNone');
				L1writeIn[i].classList.toggle('displayNone');
			}
		}
		currentL1Switch=1;
		$("#L1flash").addClass("gray");
		$("#L1flash").removeClass("orange");
	}else{
		for(let i=0;i<L1hidden.length;i++){
			if(L1hidden[i].classList.contains('displayNone')){
				L1hidden[i].classList.toggle('displayNone');
				L1writeIn[i].classList.toggle('displayNone');
			}
		}
		currentL1Switch=0;
		$("#L1flash").addClass("orange");
		$("#L1flash").removeClass("gray");
	}
});
$("#L2flash").on("click",function(){
	if(currentL2Switch==0){
		for(let i=0;i<L2hidden.length;i++){
			if(!L2hidden[i].classList.contains('displayNone')){
				L2hidden[i].classList.toggle('displayNone');
				L2writeIn[i].classList.toggle('displayNone');
			}
		}
		currentL2Switch=1;
		$("#L2flash").addClass("gray");
		$("#L2flash").removeClass("orange");
	}else{
		for(let i=0;i<L2hidden.length;i++){
			if(L2hidden[i].classList.contains('displayNone')){
				L2hidden[i].classList.toggle('displayNone');
				L2writeIn[i].classList.toggle('displayNone');
			}
		}
		currentL2Switch=0;
		$("#L2flash").addClass("orange");
		$("#L2flash").removeClass("gray");
	}
});
//スペルを一文字ごとに判定する
//イベントを作成
let clickEvent=new MouseEvent("click");
//let check=document.querySelectorAll(".check");
let L1check=document.querySelectorAll(".L1check");
let L2check=document.querySelectorAll(".L2check");
let perChar=[];
let result="<p class='correct'>";
//答え合わせ用ボタンごとにイベントを設定
function checkAndMove(check,answerArray,correctArray,writeInArray){
	for(let i=0;i<check.length;i++){
  		//エンターキーを押したときにcheckボタンをクリックするイベントを発火させる
  		answerArray[i].addEventListener('keypress',(event)=>{
  			if(event.keyCode===13){
  				check[i].dispatchEvent(clickEvent);
  			}
  		});
  		//クリックイベントを設定
  		check[i].addEventListener('click',()=>{
        let answer=answerArray[i].value;
        let correct=correctArray[i].textContent;
        let result="<span class='correct'>";
        let rightChar=[];
        //回答を一文字ずつ取得し、正解と比較
        for(let j=0;j<answer.length;j++){
        	//正解の文字列を超えた場合はbreak
      		if(j>correct.length)break;
        	//正解の文字番号を配列に格納
      		if(answer.charAt(j)==correct.charAt(j)){
            rightChar.push(j);
      		}
      	}
        //正解の文字はそのまま文字列に追加、不正解は<span>を付けて追加
        for(let k=0;k<correct.length;k++){
          if(rightChar.includes(k)){
            result+=correct.charAt(k);
          }else{
            result+=('<span class="red">'+correct.charAt(k)+'</span>');
          }
        }
        result+="</span>"
        correctArray[i].innerHTML=result;
  		//回答欄⇔答えの切り替え
  		writeInArray[i].classList.toggle('displayNone');
  		correctArray[i].classList.toggle('displayNone');
  		//次の回答欄にフォーカス
  		if(i+1<answerArray.length){
  			answerArray[i+1].focus();
  		}
  		});
  	}
}
//checkAndMoveを設定
let L1answer=document.querySelectorAll(".L1writeIn>input");
let L2answer=document.querySelectorAll(".L2writeIn>input");
checkAndMove(L1check,L1answer,L1hidden,L1writeIn);
checkAndMove(L2check,L2answer,L2hidden,L2writeIn);

//頭文字を表示する
let allAnswers=document.querySelectorAll(".writeIn>input");
let allCorrects=document.querySelectorAll(".correct");
let hint=document.getElementById("hint");
let hintSwitch=0;//0=off,1=on
hint.addEventListener("click",()=>{
	if(hintSwitch==0){
		for(let i=0;i<allCorrects.length;i++){
			let answer=allAnswers[i];
			let correct=allCorrects[i];
			answer.placeholder=correct.textContent.charAt(0);
		}
		hintSwitch=1;
		hint.classList.add("orange");
		hint.classList.remove("gray");
	}else{
		for(let i=0;i<allCorrects.length;i++){
			console.log("a");
			let answer=allAnswers[i];
			answer.placeholder="";
		}
		hintSwitch=0;
		hint.classList.add("gray");
		hint.classList.remove("orange");
	}
});

//読みあげ機能
let voice=window.speechSynthesis.getVoices();
let voiceSelect=document.getElementById("voiceSelect");
let selectedVoice;
let json;
let jsonString;
let preLang1=$("#preLang1").text();
let preLang2=$("#preLang2").text();
let preVoicename=$("#preVoicename").text();
//sessionから取得したlang1とlang2にselectedを付与
let lang1Array=document.querySelectorAll("#lang1>option");
let lang2Array=document.querySelectorAll("#lang2>option");
for(let i=0;i<lang1Array.length;i++){
	if(lang1Array[i].value==preLang1){
		lang1Array[i].selected=true;
	}
}
for(let i=0;i<lang2Array.length;i++){
	if(lang2Array[i].value==preLang2){
		lang2Array[i].selected=true;
	}
}
//voiceのoptionを作成、デバイス、ブラウザごとに異なる
for(let i=0;i<voice.length;i++){
	let option;
	//sessionから取得したvoicenameにはselectedを付与
	if(voice[i].name==preVoicename){
		option='<option value="'+voice[i].name+'" selected>'+voice[i].name+'('+voice[i].lang+')</option>' ;
	}else{
		option='<option value="'+voice[i].name+'">'+voice[i].name+'('+voice[i].lang+')</option>' ;
	}
	voiceSelect.insertAdjacentHTML("beforeend",option);
	document.getElementById("voice").textContent+=voice[i].name;
}
if("speechSynthesis" in window){
 	var L1speak=document.getElementsByClassName("L1speakButton");
	var L2speak=document.getElementsByClassName("L2speakButton");
	for(let i=0;i<L1speak.length;i++){
		L1speak[i].addEventListener("click",function(){
			let lang1=$("#lang1").val();
			let lang2=$("#lang2").val();
			const uttr=new SpeechSynthesisUtterance(L1hidden[i].textContent);
			uttr.lang=document.getElementById("lang1").value;
			uttr.volume=document.getElementById("volume").value;
			let currentVoicename=voiceSelect.value; console.log(currentVoicename);
			voice.forEach(function(v){
				if(v.name==currentVoicename){
					uttr.voice=v;
				}
			})
			//sessionから取得した言語設定と比較し、内容が変わっていたなら非同期で更新
			if(lang1!=preLang1||currentVoicename!=preVoicename){
				json={
					"lang1":lang1,
					"lang2":lang2,
					"voicename":currentVoicename
					}
				jsonString=JSON.stringify(json);
				$("#setLang").submit();
				}
			speechSynthesis.speak(uttr);
			console.log(uttr);

		});
	}
	for(let i=0;i<L2speak.length;i++){
    	L2speak[i].addEventListener("click",function(){
			let lang1=$("#lang1").val();
			let lang2=$("#lang2").val();
			const uttr=new SpeechSynthesisUtterance(L2hidden[i].textContent);
			uttr.lang=document.getElementById("lang2").value;
			uttr.volume=document.getElementById("volume").value;
			let currentVoicename=voiceSelect.value;
			voice.forEach(function(v){
				if(v.name==currentVoicename)
				uttr.voice=v;
			})
			if(lang2!=preLang2||currentVoicename!=preVoicename){
				json={
					"lang1":lang1,
					"lang2":lang2,
					"voicename":currentVoicename
					}
				jsonString=JSON.stringify(json);
				$("#setLang").submit();
			}
    		speechSynthesis.speak(uttr);
		});
	}
}else{
	console.log("false");
	$("#speechNotFound").removeClass("displayNone");
}

$(function(){
	let token=$("meta[name='_csrf']").attr("content");
	let header=$("meta[name='_csrf_header']").attr("content");
	$(document).ajaxSend(function(e,xhr,options){
		xhr.setRequestHeader(header,token);
	});
	$("#setLang").on("submit",function(e){
		e.preventDefault();

		$.ajax({
			url:$(this).attr("action"),
			type:"POST",
			data:jsonString,
			dataType:"json",
			contentType:"application/json"
		})
	})
})

//メニューの切り替えを設定
let premenu=0;
let postmenu;
function changeMenu(pre,post){
	console.log(pre);
	console.log(post);
	//premenuを非表示にする
	if(pre==0){
		$(".operationMenu").toggleClass("displayNone");
		$(".operationMenuButton").toggleClass("selected");
		$(".operationMenuButton").toggleClass("unselected");
		$(".operationMenuButton").toggleClass("pointer");
	}
	if(pre==1){
		$(".tagMenu").toggleClass("displayNone");
		$(".tagMenuButton").toggleClass("unselected");
		$(".tagMenuButton").toggleClass("selected");
		$(".tagMenuButton").toggleClass("pointer");
	}
	if(pre==2){
		$(".logMenu").toggleClass("displayNone");
		$(".logMenuButton").toggleClass("unselected");
		$(".logMenuButton").toggleClass("selected");
		$(".logMenuButton").toggleClass("pointer");
	}
	//postmenuを表示する
	if(post==0){
		$(".operationMenu").toggleClass("displayNone");
		$(".operationMenuButton").toggleClass("selected");
		$(".operationMenuButton").toggleClass("unselected");
		$(".operationMenuButton").toggleClass("pointer");
	}
	if(post==1){
		$(".tagMenu").toggleClass("displayNone");
		$(".tagMenuButton").toggleClass("unselected");
		$(".tagMenuButton").toggleClass("selected");
		$(".tagMenuButton").toggleClass("pointer");
	}
	if(post==2){
		$(".logMenu").toggleClass("displayNone");
		$(".logMenuButton").toggleClass("unselected");
		$(".logMenuButton").toggleClass("selected");
		$(".logMenuButton").toggleClass("pointer");
	}
}
var operationMenu=document.querySelectorAll(".operationMenu");
$(".operationMenuButton").on("click",function(){
	postmenu=0;
	if(premenu===0){
		return;
	}else{
		changeMenu(premenu,postmenu);
	}
	premenu=postmenu;
	console.log("operation:"+premenu+","+postmenu);
});
var tagMenu=document.querySelectorAll(".tagMenu");
$(".tagMenuButton").on("click",function(){
	postmenu=1;
	if(premenu===1){
		return;
	}else{
		changeMenu(premenu,postmenu);
	}
	premenu=postmenu;
	console.log("tag:"+premenu+","+postmenu);
});
$(".logMenuButton").on("click",function(){
	postmenu=2;
	if(premenu===2){
		return;
	}else{
		changeMenu(premenu,postmenu);
	}
	premenu=postmenu;
	console.log("log:"+premenu+","+postmenu);
});

//menuの追従、スマホ用の設定
function adjustMenu(){
	if(screen.width<480){
		$(".menuForMobile").toggleClass("displayNone");
		$(".float-container").toggleClass("displayNone");
		$(".menuForMobile").on("click",function(){
			console.log("a");
			$(".float-container").toggleClass("displayNone");
		});
	}
}
window.addEventListener("load",adjustMenu);

/*else{
	//css の position: sticky が反映されないため、javascript で実装
	//画面全体の高さを取得し、その15%の高さに設定する。操作メッセージも考慮し、15%をstickyの境界にする
	//menuの最初の位置の高さを設定
	let firstHeight=screen.availHeight*0.15;
	console.log(firstHeight);
	$('.menu-container').css('position',firstHeight+"px");
	var menuContainer=document.querySelector(".menu-container");
	menuContainer.style.top=firstHeight+"px";
	console.log($('.menu-container').offset().top);
	$(window).on('scroll',function(){
		let secondHeight=$('.menu-container').offset().top;
		//スクロール量を取得
		let scroll=$(window).scrollTop();
		//スクロール量がmenuの高さを上回ったらstickyを追加、下回ったら削除
		if(scroll>firstHeight){
			menuContainer.style.position="fixed";
			//$(".menu-container").addClass('sticky');
		}else{
			//$(".menu-container").removeClass('sticky');
			menuContainer.style.position="";
		}
	});
}*/

$("#scrollTop").on("click",function(){
	window.scrollTo(0,0);
});
var container=document.querySelector(".container");
var bottom=container.scrollHeight-container.clientHeight;
$("#scrollBottom").on("click",function(){
	console.log(container.scrollHeight);
	console.log(container.clientHeight);
	window.scrollTo(0,container.scrollHeight);
});

