async function login() {
    let username = document.getElementById("username").value;
    let password = document.getElementById("password").value;
  
    // Aqui não foi validade se os campos username e password são válidos (se não são vazios, por exemplo)
  
    const response = await fetch("http://localhost:8080/login", {
      method: "POST",
      headers: new Headers({
        "Content-Type": "application/json; charset=utf8",
        Accept: "application/json",
      }),

      // Converte o javascript para json para enviar no corpo da requisição.
      body: JSON.stringify({
        username: username,
        password: password,
      }),
    });
  
    let key = "Authorization";
    let token = response.headers.get(key);      // recupera o token que vem pelo header na respota da requisição.
    window.localStorage.setItem(key, token);
  
    if (response.ok) {
      showToast("#okToast");
    } else {
      showToast("#errorToast");
    }
    
    // Redireciona o usuário para a área logada em 2 segundos.
    window.setTimeout(function () {
      window.location = "/view/index.html";
    }, 2000);
  }
  
  function showToast(id) {
    let toastElList = [].slice.call(document.querySelectorAll(id));
    let toastList = toastElList.map(function (toastEl) {
      return new bootstrap.Toast(toastEl);
    });
    
    toastList.forEach((toast) => toast.show());
  }