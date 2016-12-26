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
        location.reload();
    });
}

function deleteLink(matchId){
    $.post(context + 'contacts/deleteLink', 'matchId=' + matchId, function () {
        location.reload();
    });
}

