/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

function show_images() {
    var user = sessionStorage.getItem('userId');
    if (!user)user = 4;

    $.ajax({
        url: "http://127.0.0.1:8080/image-sharing-service/mypictures",
        data: {user: user},
        success: function (data) {
            var arr = JSON.parse(data);
            var html = [];
            for (i = 0; i < arr.length; i++) {
                html.push('<div class="col-lg-3 col-md-4 col-xs-6"><a href="#" onclick="read_image(' + arr[i].id + ');return false;" class="thumbnail">');
                html.push('<img class="img-responsive" src="images/' + arr[i].path + '" /></a>');
                html.push('</div>');
            }
            $("#myimages").empty().append(html.join(''));
        }
    });
};