async function signup() {
    let username = document.getElementById("username").value;
    let password = document.getElementById("password").value;
  
    // Aqui não foi validade se os campos username e password são válidos (se não são vazios, por exemplo)
  
    const response = await fetch("http://localhost:8080/user", {
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
  
    if (response.ok) {
        showToast("#okToast");
        // Limpa os campos do formulário
        username.value = ''
        password.value = ''                
    } else {
      showToast("#errorToast");
    }
  }
  
  function showToast(id) {
    let toastElList = [].slice.call(document.querySelectorAll(id));
    let toastList = toastElList.map(function (toastEl) {
      return new bootstrap.Toast(toastEl);
    });

    toastList.forEach((toast) => toast.show());
  }