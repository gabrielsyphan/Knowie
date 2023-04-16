let tags = window.tagsList || [];
let answerValues = {};
let answerCount = 0;

window.onload = function() {
    let path = window.location.pathname.split("/");
    path = path[path.length - 1];

    if(path == "login") {
        localStorage.removeItem("user");
    }

    if(localStorage.getItem("user") !== null && JSON.parse(localStorage.getItem("user")).firstAccess){
       swal({
         text: "Update your password",
         content: "input",
         button: {
           text: "Update",
           closeModal: false,
         },
       }).then(pass => {
          const xhr = new XMLHttpRequest();
          xhr.onreadystatechange = function() {
             if (xhr.readyState === 4) {
                if (xhr.status >= 200 && xhr.status <= 300) {
                   const user = JSON.parse(localStorage.getItem("user"));
                   user.firstAccess = false;
                   localStorage.setItem("user", JSON.stringify(user));

                   swal({
                       title: "Success!",
                       text: "Your password has been updated.",
                       icon: "success"
                   }).then((okay) => {
                       navigate("");
                   });
                } else {
                   swal("Error!", "It was not possible to complete your registration.", "error");
                }
              }
            };

          const data = {
            id: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")).id : null,
            password: pass,
          };

          xhr.open("PUT", "/api/v1/reset-password", true);
          xhr.setRequestHeader("Content-Type", "application/json");
          xhr.send(JSON.stringify(data));
       });
    }
}

function forgotPassword() {
   swal({
     text: "Enter your email to reset your password",
     content: "input",
     button: {
       text: "Update",
       closeModal: false,
     },
   }).then(email => {
       if(email) {
           const xhr = new XMLHttpRequest();
           xhr.onreadystatechange = function() {
                if (xhr.readyState === 4) {
                   if (xhr.status >= 200 && xhr.status <= 300) {
                        const response = JSON.parse(xhr.responseText);
                        swal({
                            title: "Success!",
                            text: "Your new passowrd is:   "+ response.password,
                            icon: "success"
                        }).then((okay) => {
                            navigate("login");
                        });
                   } else {
                       swal("Error!", "It was not possible to complete your request. Please, check if the email is correct or try again later.", "error");
                   }
               }
           };

           const data = {
               email: email
           };

           xhr.open("PUT", "/api/v1/forgot-password", true);
           xhr.setRequestHeader("Content-Type", "application/json");
           xhr.send(JSON.stringify(data));
       }
   });
}

function navigate(route) {
    window.location.href = window.location.origin + "/" + route;
}

function search(e) {
    alert(userName);
    const key=e.keyCode || e.which;
    if(key == 13){
        const searchValue = document.getElementById("search").value;
        if (searchValue !== "") {
            navigate("question/" + searchValue);
        } else {
            navigate("/");
        }
    }
}

function notImplemented() {
    swal("Not implemented yet!", "This feature is not implemented yet!", "error");
}

function submitForm(e, path) {
    e.preventDefault();

    const xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function() {
        if (xhr.readyState === 4) {
            if (!xhr.responseText && path == "login") {
                swal("Error!", "These credentials do not match our records.", "error");
            }

            const response = JSON.parse(xhr.responseText);

            if (xhr.status >= 200 && xhr.status < 300) {
                if(path == "login") {
                    localStorage.setItem("user", JSON.stringify(response));
                    navigate("");
                } else if(path == "users") {
                    swal({
                        title: "Success!",
                        text: "The user password is:   "+ response.password,
                        icon: "success"
                    }).then((okay) => {
                        navigate(path);
                    });
                } else {
                    swal({
                        title: "Success!",
                        text: "Your registration has been recorded.",
                        icon: "success"
                    }).then((okay) => {
                        navigate(path);
                    });
                }
            } else {
                swal("Error!", "It was not possible to complete your request. "+ response.message.split(":")[2], "error");
            }
        }
    };

    xhr.open("PUT", "/api/v1/"+ path, true);
    xhr.setRequestHeader("Content-Type", "application/json");

    const formData = new FormData(e.target);
    const data = {};
    for (const [key, value] of formData.entries()) {
      data[key] = value;
    }

    if (path == "questions") {
        data["tags"] = tags;
        data["answerValues"] = [
            answerValues.option1,
            answerValues.option2,
            answerValues.option3,
            answerValues.option4
        ];
    }

   if (path == "exams") {
       data["questions"] = selectedQuestions;
   }

    xhr.send(JSON.stringify(data));
}

function removeData(id, path) {
    swal({
        title: "Are you sure?",
        text: "Once deleted, you will not be able to recover this data!",
        icon: "warning",
        buttons: true,
        dangerMode: true,
    })
    .then((willDelete) => {
        if (willDelete) {
            const xhr = new XMLHttpRequest();
            xhr.onreadystatechange = function() {
                if (xhr.status >= 200 && xhr.status < 300) {
                    swal("Success!", "The user has been deleted.", "success");
                    setTimeout(function() {
                        navigate(path);
                    }, 1000);
                } else {
                    const response = JSON.parse(xhr.responseText);
                    swal("Error!", "It was not possible to delete the user. Json: "+ response.message.split(":")[1], "error");
                }
            };

            xhr.open("DELETE", "/api/v1/"+ path +"/" + id, true);
            xhr.setRequestHeader("Content-Type", "application/json");
            xhr.send();
        }
    });
}

function showNav() {
    const menu = document.getElementById("nav");
    if (menu.style.display === "grid") {
        menu.style.display = "none";
    } else {
        menu.style.display = "grid";
    }
}

function addTag(tag) {
    if(tag.length > 0) {
        if (tags.length < 5) {
            tags.push(tag);
            updateTags();
            document.querySelector('input[name=tag]').value = '';
        } else {
            swal("Error!", "You can only add 5 tags.", "error");
        }
    }
}

function updateTags() {
    const tagArea = document.querySelector('.tag-area');
    tagArea.innerHTML = '';
    tags.forEach(tag => {
        const tagElement = document.createElement('a');
        tagElement.classList.add('ui', 'label');
        tagElement.textContent = tag;
        const closeIcon = document.createElement('i');
        closeIcon.classList.add('icon', 'close');
        tagElement.appendChild(closeIcon);
        tagArea.appendChild(tagElement);

        closeIcon.addEventListener('click', () => {
            removeTag(tag);
        });

        tagElement.addEventListener('click', () => {
            removeTag(tag);
        });
    });
}

function removeTag(tag) {
    const index = tags.indexOf(tag);
    if (index !== -1) {
        tags.splice(index, 1);
        updateTags();
    }
}

function addInputs(answerType) {
    let inputContainer = document.getElementById("answerInputs");
    inputContainer.innerHTML = "";

    if (answerType == "2" || answerType == "3") {
        inputContainer.innerHTML = `
            <div class="field">
                <label>Choose nº1</label>
                <input type="text" name="answerChoice" onchange="answerValues.option1 = this.value" required>
            </div>
            <div class="field">
                <label>Choose nº2</label>
                <input type="text" name="answerChoice" onchange="answerValues.option2 = this.value" required>
            </div>
            <div class="field">
                <label>Choose nº3</label>
                <input type="text" name="answerChoice" onchange="answerValues.option3 = this.value">
            </div>
            <div class="field">
                <label>Choose nº4</label>
                <input type="text" name="answerChoice" onchange="answerValues.option4 = this.value">
            </div>
        `;
      }
}

function addAnswerChoices(answers) {
    let inputContainer = document.getElementById("answerInputs");
    inputContainer.innerHTML = "";

    if(answers.length > 0) {
        answers.forEach(answer => {
            answerCount++;
            inputContainer.innerHTML += `
                <div class="field">
                    <label>Choose nº${answerCount}</label>
                    <input type="text" name="answerChoice" onchange="answerValues.option${answerCount} = this.value" value="${answer}" required>
                </div>
            `;
        });
    }
}

document.getElementById("answerType").addEventListener("change", function() {
    addInputs(this.value);
});
