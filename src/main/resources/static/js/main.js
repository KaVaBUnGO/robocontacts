// add CSRF token to all ajax requests
$(function () {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    $(document).ajaxSend(function (e, xhr, options) {
        xhr.setRequestHeader(header, token);
    }).ajaxError(function (e, xhr) {
        if (xhr.status == 403) {
            $('#serverErrorModal').modal('show');
        }
    });
});
