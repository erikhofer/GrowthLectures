var slugDidChange = false;
var slugAtBegin;

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
          console.log(data);
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

function showErrors(errorList) {
    $.each(errorList, function(key, value) {
      $("#new-category-errors").append("<p>"+value+"</p>");      
    });
    $("#new-category-errors").removeClass("hidden");
}

function showError(error) {
  showErrors([error]);
}

$("#input-category-slug").focus(function() {
  slugAtBegin = $(this).val();
});

$("#input-category-slug").blur(function() {
  if(!slugDidChange) {
    if(slugAtBegin != $(this).val()) {
      slugDidChange = true;
    }    
  }
});

$("#input-category-title").keyup(function() {
  if(!slugDidChange)   
    $("#input-category-slug").val(slugify($(this).val()));
});