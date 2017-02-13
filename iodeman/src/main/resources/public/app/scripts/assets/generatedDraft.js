/**
 * Created by pauline on 18/01/17.
 */

function show_hide_creneaux(){
    $('.line_creneaux').each(function(){
        var occupated = false;
        console.log($(this));
        $($(this).children('td')).each(function(item){
            if ($(this).children().length >0){
                occupated = true;
            }
        })
        if(!occupated){
            if($(this).is(":visible")){
                $(this).hide();

            }else{
                $(this).show();
            }
        }

    });

    if($("#rotate i").hasClass("glyphicon-chevron-down")){
        $("#rotate i").addClass("glyphicon-chevron-up");
        $("#rotate i").removeClass("glyphicon-chevron-down");
    }
    else{
        $("#rotate i").addClass("glyphicon-chevron-down");
        $("#rotate i").removeClass("glyphicon-chevron-up");
    }



}


