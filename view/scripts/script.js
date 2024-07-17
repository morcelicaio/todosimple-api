// Script puro em javascript para carregamento da tabela de tarefas do usuário.

//const url = "http://localhost:8080/task/user/1"  hard coded
const taskEndPoint = "http://localhost:8080/task/user"

function hideLoader(){
    document.getElementById("loading").style.display = "none";
}


function showTableTasks(tasks){
    // <th scope="col">Username</th>
    // <th scope="col">User Id</th>

    // cabeçalho da tabela
    let table = `
        <thead>
            <th scope="col">Task Id</th>
            <th scope="col">Description</th>            
        </thead>
    `
    // dados do corpo (<tbody></tbody>) da tabela
    for (let task of tasks) {        
        //<td scope="row">${task.user.username}</td>
        //td scope="row">${task.user.id}</td>

        table += `
            <tr>
                <td scope="row">${task.id}</td>                        
                <td scope="row">${task.description}</td>                                                        
            </tr>
        `
    }

    document.getElementById("tasks").innerHTML = table
}

// Realiza a busca das tasks do usuário que está autenticado no sistema.
async function getTasks(){
    let key = "Authorization"

    const response = await fetch(
        taskEndPoint, { 
            method: "GET" ,
            headers: new Headers({
                Authorization: localStorage.getItem(key)
            })
        }
    )

    let data = await response.json();

    if(response){
        hideLoader()
        showTableTasks(data)
    }
}


document.addEventListener("DOMContentLoaded", function(event){
    if(!localStorage.getItem("Authorization")){
        window.location = "/view/login.html"
    }
})


getTasks()


function doSignOut(){
    // Remove o token e redireciona o usuário para a tela de login.
    localStorage.removeItem('Authorization')
    window.location = "/view/login.html"
}

/*
async function getAPI(urlAPI){
    const response = await fetch(urlAPI, { method: "GET" })

    let data = await response.json()    

    if(response){
        hideLoader()
        showTableTasks(data)
    }
}

getAPI(url)
*/