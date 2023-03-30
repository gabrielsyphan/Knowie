function navigate(route) {
    window.location.href = window.location.origin + "/" + route;
}

function search(e) {
    const key=e.keyCode || e.which;
    if(key == 13){
        const searchValue = document.getElementById("search").value;
        if (searchValue !== "") {
            navigate("order/" + searchValue);
        } else {
            navigate("order");
        }
    }
}

function notImplemented() {
    swal("Not implemented yet!", "This feature is not implemented yet!", "error");
}
