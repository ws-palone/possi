/**
 * Created by pauline on 18/01/17.
 */

function show_hide_creneaux(){
    $('.line_creneau').each(function(){
        if($($(this).children('.creneau')[0]).children().length == 0){
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
