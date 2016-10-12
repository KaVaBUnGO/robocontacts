/**
 * Created by ekaterina on 12.10.2016.
 */
function addUser() {
    $('#userDialog').dialog("option", "title", 'Добавить пользователя');
    $('#userDialog').dialog('open');
}

function editUser(id) {

    $.get("/users/get/" + id, function(result) {

        $("#userDialog").html(result);

        $('#userDialog').dialog("option", "title", 'Edit User');

        $("#userDialog").dialog('open');
    });
}

function deleteUser(id) {

    $( "#dialog-confirm" ).dialog({
        resizable: false,
        height:200,
        width: 300,
        modal: true,
        buttons: {
            "Delete user": function() {
                $.ajax({
                    url: "/users/delete/" + id,
                    type: "POST",
                    success:function(response) {
                        $('#user-'+id).hide()
                    }
                });
                $( this ).dialog( "close" );
            },
            Cancel: function() {
                $( this ).dialog( "close" );
            }
        }
    });
}

function resetDialog(form) {

    form.find("input").val("");
}

$(document).ready(function() {

    $('#userDialog').dialog({

        autoOpen : false,
        position : 'center',
        modal : true,
        resizable : false,
        width : 440,
        buttons : {
            "Сохранить" : function() {
                $('#userForm').submit();
            },
            "Отменить" : function() {
                $(this).dialog('close');
            }
        },
        close : function() {

            resetDialog($('#userForm'));

            $(this).dialog('close');
        }
    });
});

$(function () {
    var token = $("input[name='_csrf']").val();
    var header = "X-CSRF-TOKEN";
    $(document).ajaxSend(function(e, xhr, options) {
        xhr.setRequestHeader(header, token);
    });
});