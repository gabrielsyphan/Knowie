function navigate(route) {
    window.location.href = window.location.origin + "/" + route;
}

function search(e) {
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
        if (xhr.status >= 200 && xhr.status < 300) {
            e.target.reset();
            swal("Success!", "Your registration has been recorded.", "success");
            setTimeout(function() {
                navigate("users");
            }, 2000);
        } else {
            const response = JSON.parse(xhr.responseText);
            swal("Error!", "It was not possible to complete your registration. Json: "+ response.message.split(":")[1], "error");
        }
    };

    xhr.open("PUT", "/api/v1/"+ path, true);
    xhr.setRequestHeader("Content-Type", "application/json");

    const formData = new FormData(e.target);
    const data = {};
    for (const [key, value] of formData.entries()) {
      data[key] = value;
    }

    xhr.send(JSON.stringify(data));
}

function removeUser(id) {
    swal({
        title: "Are you sure?",
        text: "Once deleted, you will not be able to recover this user!",
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
                        navigate("users");
                    }, 2000);
                } else {
                    const response = JSON.parse(xhr.responseText);
                    swal("Error!", "It was not possible to delete the user. Json: "+ response.message.split(":")[1], "error");
                }
            };

            xhr.open("DELETE", "/api/v1/users/" + id, true);
            xhr.setRequestHeader("Content-Type", "application/json");
            xhr.send();
        }
    });
}

function showNav() {
    const menu = document.getElementById("nav");
    if (menu.style.display === "none") {
        menu.style.display = "grid";
    } else {
        menu.style.display = "none";
    }
}