const search = () => {
    let keyword = document.getElementById("search-input").value
    if (keyword == "") {
        $("#search-result").html("");
        return
    }
    var regex = /^\d+$/;

    if (regex.test(keyword)) {
        if (keyword.length >= 3) {
            url = "/user/contact/search/phone/" + keyword
            fetch(url).then((response) => {
                return response.json();
            }).then((data) => {
                $("#search-result").html("");
                var resultHtml = "";
                if (data.length > 0) {
                    for (var i = 0; i < data.length; i++) {
                        var phone = highlightMatch(data[i].phone, keyword);
                        resultHtml += `<a href="/user/contact/details/${data[i].id}" class="list-group-item list-group-item-action">${phone}</a>`;
                    }
                    $("#search-result").html(resultHtml);
                }
            });
        }else{
            $("#search-result").html("");
        }

    } else {
        url = "/user/contact/search/name/" + keyword
        fetch(url).then((response) => {
            return response.json();
        }).then((data) => {
            $("#search-result").html("");
            var resultHtml = "";
            if (data.length > 0) {
                for (var i = 0; i < data.length; i++) {
                    var name = highlightMatch(data[i].name, keyword);
                    resultHtml += `<a href="/user/contact/details/${data[i].id}" class="list-group-item list-group-item-action">${name}</a>`;
                }
                $("#search-result").html(resultHtml);
            }
        });
    }

}

const highlightMatch = (text, keyword) => {
    let regex = new RegExp(keyword, "gi");
    return text.replace(regex, `<span class="highlight">${keyword}</span>`);
}

const enableUserEditing = () =>{
    $("#name").prop("readonly", false);
    $("#email").prop("readonly", false);
    $("#about").prop("readonly", false);
    $("#profileImageField").show();
    $("#editBtn").css("display", "none");
    $("#cancelBtn").css("display", "inline-block");
    $("#submitBtn").css("display", "inline-block");

}

const cancelUserUpdate = () =>{
    $("#name").prop("readonly", true);
    $("#email").prop("readonly", true);
    $("#about").prop("readonly", true);
    $("#profileImageField").hide();
    $("#editBtn").css("display", "inline-block");
    $("#cancelBtn").css("display", "none");
    $("#submitBtn").css("display", "none");

}

