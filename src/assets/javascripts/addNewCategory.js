$("form#newCategory").submit(function(e){
  
    $("#new-category-errors").addClass("hidden");
    $("#new-category-success").addClass("hidden");
    $("#new-category-errors").html("");
    
    formData = new FormData($(this)[0]);
  
    form = $(this);
    var requestUrl = form.attr("action");
    $.ajax({
        url: requestUrl,
        type: 'POST',
        data: formData,
        async: false,
        cache: false,
        contentType: false,
        processData: false,
        timeout : 100000,
        success : function(data) {
          // Show success msg
          $("#new-category-success").removeClass("hidden");
          
          // Show Link to created Video
          $("#new-category-link").attr("href", data);
          $("#new-category-success").removeAttr("hidden");
          
          // Reset Form
          form.trigger("reset");          
        },
        error : function(e) {
          errorMsgs = e.responseJSON;
          showErrors(errorMsgs);
        },
        done : function(e) {
          console.log("DONE");
        }
    });
    e.preventDefault();
});
