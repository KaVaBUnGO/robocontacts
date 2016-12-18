function connectNewPlatform(platform){
    $.post(context + 'profile/connectPlatform', 'platform=' + platform, function (data) {
        window.location.href = data;
    });
}

function deletePlatform(platform){
    $.post(context + 'profile/deletePlatform', 'platform=' + platform, function () {
        location.reload();
    });
}

function addFriendsConnection(){
    var vkId = $('select[name=vkId]').val();
    var googleId = $('select[name=googleId]').val();
    $.post(context + 'contacts/save', 'vkId='+vkId+"&googleId="+googleId, function (data) {
        window.location.href = data;
    });
}
