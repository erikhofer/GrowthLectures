var slugDidChange = false;
var slugAtBegin;

$("form#newLecturer").submit(function(e){
  
    $("#new-lecturer-errors").addClass("hidden");
    $("#new-lecturer-success").addClass("hidden");
    $("#new-lecturer-errors").html("");
    
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
          $("#new-lecturer-success").removeClass("hidden");
          
          // Show Link to created Video
          $("#new-lecturer-link").attr("href", data);
          $("#new-lecturer-success").removeAttr("hidden");
          
          // Reset Form
          form.trigger("reset");          
        },
        error : function(e) {
          errorMsgs = e.responseJSON;
          showErrors(errorMsgs);
        },
        done : function(e) {
        }
    });
    
    e.preventDefault();
});

function showErrors(errorList) {
    $.each(errorList, function(key, value) {
      $("#new-lecturer-errors").append("<p>"+value+"</p>");      
    });
    $("#new-lecturer-errors").removeClass("hidden");
}

function showError(error) {
  showErrors([error]);
}

$("#input-lecturer-slug").focus(function() {
  slugAtBegin = $(this).val();
});

$("#input-lecturer-slug").blur(function() {
  if(!slugDidChange) {
    if(slugAtBegin != $(this).val()) {
      slugDidChange = true;
    }    
  }
});

$("#input-lecturer-title").keyup(function() {
  if(!slugDidChange)   
    $("#input-lecturer-slug").val(slugify($(this).val()));
});