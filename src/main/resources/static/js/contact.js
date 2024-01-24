// include jquery using link

$(document).ready(function () {

    $("#phone").on("input", function () {
        if ($(this).val().length == 0 || $(this).val() == null) {
            $("#phone").addClass("is-invalid");
            $("#invalidPhone").html("Cannot be empty");
            $("#submitBtn").prop("disabled", true);
        }
        else if (validPhoneNumber($(this).val())) {
            if ($(this).val().length == 10) {
                $("#invalidPhone").html("");
                $("#phone").removeClass("is-invalid");
                $("#submitBtn").prop("disabled", false);
            }
            else {
                $("#phone").addClass("is-invalid");
                $("#invalidPhone").html("Must be 10 characters long");
                $("#submitBtn").prop("disabled", true);
            }
        } else {
            $("#phone").addClass("is-invalid");
            $("#invalidPhone").html("Must starts with 6-9");
            $("#submitBtn").prop("disabled", true);
        }

    });

    $("#profileImage").on("change", function () {
        const maxSize = 2 * 1024 * 1024;
        if (this.files[0].size > maxSize) {
            $("#invalidProfileImage").html("Image size must be less then 2MB");
            $("#profileImage").val("");
            $("#profileImage").addClass("is-invalid");
            $("#submitBtn").prop("disabled", true);
        }
        else {
            $("#profileImage").removeClass("is-invalid");
            $("#invalidProfileImage").html("");
            $("#submitBtn").prop("disabled", false);
        }

        const previewImage = document.getElementById('previewImage');

        if (this.files && this.files[0]) {
            const reader = new FileReader();

            reader.onload = function (e) {
                previewImage.src = e.target.result;
            };

            reader.readAsDataURL(this.files[0]);
        }

    });

});

const validPhoneNumber = function (phone) {
    var regex = /^[6-9]\d*$/;
    if (regex.test(phone)) {
        return true
    }
    else {
        return false
    }
}

function showContact(element) {
    var contactId = element.getAttribute('data-id');
    $("#id").val(contactId);
    $("#work").val(element.getAttribute('data-work'));
    $("#description").val(element.getAttribute('data-desc'));
    var contactImage = element.getAttribute('data-image');
    if (contactImage == "default.png" || contactImage == null) {
        $("#profilePic").attr("src", "/img/default_profile_pic.png");
    }
    else {
        $("#profilePic").attr("src", "/img/users/" + userName + "/" + contactImage);
    }

    $('#modal-contactName').text($(element).find('#' + contactId + '-contactName').text());
    $('#name').val($(element).find('#' + contactId + '-contactName').text());
    $('#phone').val($(element).find('#' + contactId + '-contactPhone').text());
    $('#email').val($(element).find('#' + contactId + '-contactEmail').text());
    $('#address').val($(element).find('#' + contactId + '-contactAddress').text());

    // IF USER has clicked on edit button
    // if ($("#editBtn").css("display") == "none") {
    //     cancelUpdate();
    // }
}

function enableContactEditing() {
    $("#contactImage").css("display", "none");
    $("#nameField").css("display", "block");
    $("#imageField").css("display", "block");
    $("#phone").prop("disabled", false);
    $("#email").prop("disabled", false);
    $("#address").prop("disabled", false);
    $("#work").prop("disabled", false);
    $("#description").prop("disabled", false);
    $("#updateHeading").css("display", "block");
    $("#submitBtn").css("display", "block");
    $("#editBtn").css("display", "none");
    $("#deleteBtn").css("display", "none");
    $("#cancelBtn").css("display", "block");

}

// on cancel click disable editing
function cancelContactUpdate() {
    $("#contactImage").css("display", "block");
    $("#nameField").css("display", "none");
    $("#imageField").css("display", "none");
    $("#phone").prop("disabled", true);
    $("#email").prop("disabled", true);
    $("#address").prop("disabled", true);
    $("#work").prop("disabled", true);
    $("#description").prop("disabled", true);
    $("#updateHeading").css("display", "none");
    $("#submitBtn").css("display", "none");
    $("#editBtn").css("display", "block");
    $("#deleteBtn").css("display", "block");
    $("#cancelBtn").css("display", "none");
}

function deleteContactBtn() {
    if (confirm("Are you sure you want to delete this contact?")) {
        deleteContact($("#id").val());
    }
}

editContactIcon = function () {
    enableContactEditing();
}

deleteContactIcon = function (element) {
    if (confirm("Are you sure you want to delete this contact?")) {
        deleteContact(element.getAttribute('data-id'));
    }
}
function deleteContact(id) {
    $.ajax({
        url: '/user/contact/delete/' + id,
        type: 'DELETE',
        success: function (response) {
            if (response) {
                // alert("Contact deleted Successfuly 👍");
                location.reload();
            }
            else {
                // alert("Faild to delete contact 🥲")
            }
        },
        error: function (error) {
            alert("Faild to delete contact");
        }
    });
}

/* const updateContact = function () {
    // ajax call to save updated contact
    var formData = new FormData($('#contactForm')[0]);
    $.ajax({
        url: '/user/contact/update',
        type: 'PUT',
        data: formData,
        success: function (response) {
            if (response) {
                alert("Contact updated Successfuly 👍");
                location.reload();
            }
            else {
                alert("Faild to update contact 🥲")
            }
        },
        error: function (error) {
            alert("Faild to update contact" + error);
        }
    });
} */

function updateContact() {
    // Create a FormData object
    var formData = new FormData($('#contactForm')[0]);

    // Make an AJAX request
    $.ajax({
        url: '/user/contact/update',  // Replace with your actual server endpoint
        type: 'PUT',
        data: formData,
        contentType: false,
        processData: false,
        success: function (response) {

            location.reload();
        },
        error: function (error) {
            // if error is saying put method is not allowed
            alert("Your login session has expired. Please login again.");
            location.reload();
        }
    });
}
