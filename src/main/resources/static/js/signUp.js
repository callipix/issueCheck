document.getElementById("checkLoginBtn")
    .addEventListener("click", async  () => {

        const loginId = document.getElementById("loginId").value;
        const resultCheck = document.getElementById("loginIdCheckResult");

        if (!/^(?=.*[a-zA-Z])[a-zA-Z0-9]{8,12}$/.test(loginId)) {
            resultCheck.textContent = "아이디 형식을 확인해 주세요.";
            resultCheck.className = "form-text text-danger";
            return;
        }

        console.log(loginId)
        try {
            const response = await fetch(
                `/auth/check-loginId?loginId=${encodeURIComponent(loginId)}`
            );

            if(!response.ok){
                throw new Error("중복 확인 요청 실패");
            }

            const result = await response.json();

            if(result.duplicated){
                resultCheck.textContent = "이미 사용 중인 아이디입니다.";
                resultCheck.className = "form-text text-danger";
            } else {
                resultCheck.textContent = "사용 가능한 아이디입니다."
                resultCheck.className = "form-text text-success";
            }
        }
        catch(error){
            resultCheck.textContent = "중복 확인 중 오류가 발생했습니다.";
            resultCheck.className = "form-text text-success";
        }
    });

document.getElementById("checkNicknameBtn")
    .addEventListener("click", async  () => {

        const nickname = document.getElementById("nickname").value;
        const resultCheck = document.getElementById("nicknameCheckResult");

        console.log("닉네임: "+nickname)
        if (!/^[가-힣a-zA-Z0-9]{2,20}$/.test(nickname)) {
            resultCheck.textContent = "닉네임 형식을 확인해 주세요.";
            resultCheck.className = "form-text text-danger";
            return;
        }
        try {
            const response = await fetch(
                `/auth/check-nickname?nickname=${encodeURIComponent(nickname)}`
            );

            if(!response.ok){
                throw new Error("중복 확인 요청 실패");
            }

            const result = await response.json();
            console.log(result)
            if(result.duplicated){
                resultCheck.textContent = "이미 사용 중인 닉네임입니다.";
                resultCheck.className = "form-text text-danger";
            } else {
                resultCheck.textContent = "사용 가능한 닉네임입니다."
                resultCheck.className = "form-text text-success";
            }
        }
        catch(error){
            resultCheck.textContent = "중복 확인 중 오류가 발생했습니다.";
            resultCheck.className = "form-text text-success";
        }
    });

document.getElementById("checkEmailBtn")
    .addEventListener("click", async  () => {

        const email = document.getElementById("email").value;
        const resultCheck = document.getElementById("emailCheckResult");

        console.log("이메일: "+ email)
        if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) {
            resultCheck.textContent = "이메일 형식을 확인해 주세요.";
            resultCheck.className = "form-text text-danger";
            return;
        }
        try {
            const response = await fetch(
                `/auth/check-email?email=${encodeURIComponent(email)}`
            );

            if(!response.ok){
                throw new Error("중복 확인 요청 실패");
            }

            const result = await response.json();
            console.log(result)
            if(result.duplicated){
                resultCheck.textContent = "이미 사용중인 이메일입니다.";
                resultCheck.className = "form-text text-danger";
            } else {
                resultCheck.textContent = "사용 가능한 이메일입니다."
                resultCheck.className = "form-text text-success";
            }
        }
        catch(error){
            resultCheck.textContent = "중복 확인 중 오류가 발생했습니다.";
            resultCheck.className = "form-text text-success";
        }
    });