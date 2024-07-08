// Script puro em javascript para carregamento da tabela de tarefas do usuário.

const url = "http://localhost:8080/task/user/1"

function hideLoader(){
    document.getElementById("loading").style.display = "none";
}


function showTableTasks(tasks){
    // cabeçalho da tabela
    let table = `
        <thead>
            <th scope="col">Task Id</th>
            <th scope="col">Description</th>
            <th scope="col">Username</th>
            <th scope="col">User Id</th>
        </thead>
    `
    // dados do corpo (<tbody></tbody>) da tabela
    for (let task of tasks) {        
        table += `
            <tr>
                <td scope="row">${task.id}</td>                        
                <td scope="row">${task.description}</td>                        
                <td scope="row">${task.user.username}</td>                        
                <td scope="row">${task.user.id}</td>
            </tr>
        `
    }

    document.getElementById("tasks").innerHTML = table
}

async function getAPI(urlAPI){
    const response = await fetch(urlAPI, { method: "GET" })

    let data = await response.json();    

    if(response){
        hideLoader()
        showTableTasks(data)
    }
}

getAPI(url)