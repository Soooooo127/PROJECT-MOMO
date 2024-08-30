function resetPw2() {
    var modifyPw1 = $("#modifyPw1").val();
    var modifyPw2 = $("#modifyPw2").val();

    if(modifyPw1 == modifyPw2) {

      alert("같습니다");

      $.ajax({
         url:"/mail/resetPw",
         type:"post",
         crossDomain:true,
         data:{
           "memberid" : $("#memberid").val(),
           "password" : modifyPw1
         },

         error:function(request, status, error){
           alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
         },
        
         success : function() {
           alert("성공");
           window.location.href = "/member/modifyMember";
         }
  
       });

    } else {

      const checkValid = document.querySelector("#modifyPw2").classList.add("is-invalid");
      alert("비밀번호를 다르게 입력하였습니다. 다시 입력해주세요");
    }


}

