$(function () {
    $("#publishBtn").click(publish);
});

function publish() {
    $("#publishModal").modal("hide");
    let content = $("#message-text").val();
    let title = $("#recipient-name").val();
    $.post(
        "/discuss/add",
        {
            "title": title,
            "content": content
        },
        (data) => {
            data = $.parseJSON(data);
            console.log(data);
            let hintModel = $("#hintModal");
            $("#hintBody").text(data.msg);
            hintModel.modal("show");
            setTimeout(function () {
                $("#hintModal").modal("hide");
                if (data.code==200){
                    window.location.reload();
                }
            }, 2000);
        }
    )

}