var YOUTUBE_EXP = /https?:\/\/((.+\.)?youtube.com|youtu.be)\/(.+)/i;
var VIMEO_EXP = /https?:\/\/(.*\.)?vimeo.com\/(video\/)?(\d+)(\?(.*))?/i;

var slugDidChange = false;
var slugAtBegin;

$("#new-lecture-continue").click(function() {
  
  hideErrors();
  slugDidChange = false;
  
  var link = $("#input-lecture-link").val();
  if(link.match(YOUTUBE_EXP)) {
    var data;
    if(isValidYoutubeVideo(link)) {
      youtubeId = extractYoutubeId(link);
      data = getYoutubeVideoData(youtubeId);
      finalData = correctYoutubeStartEnd(data, link);
    } else {
      showError("Link is not a Valid Link");
      return;
    }
  } else if(link.match(VIMEO_EXP)) {
    showError("Vimeo is currently not supported");
    return;
  } else {
    showError("Videoplattform is not known");
    return; 
  }
  
  if(finalData != null && finalData["title"] != "") {
    
    fillDataInForm(finalData);
    $("#new-lecture-link-form").addClass("hidden");
    $("#new-lecture-data-form").removeClass("hidden");
    
    $("#new-lecture-continue").attr("disabled", "true");
    $("#new-lecture-submit").removeAttr("disabled");
  } else {
    showError("Video could not be found!");
    return
  }
});

function correctYoutubeStartEnd(data, path) {
  
  var start = /(\?|\&)(start|t)=(\d+)/i.exec(path);
  var end = /(\?|\&)(end)=(\d+)/i.exec(path);      
    
  if(start == null) {
    data["start"] = 0;
  } else {
    data["start"] = start[3];
  }
  
  data["duration"] = data["duration"] - data["start"];
  if(end != null) {
    data["duration"] = end[3] - data["start"];
  }
  
  return data;
}

function extractYoutubeId(link) {
  var path = /https?:\/\/((.+\.)?youtube.com|youtu.be)\/(.+)/i.exec(link)[3];
  var youtubeId = /((\?|\&)(v)=|^|embed\/)([a-zA-Z0-9-_]{11})/.exec(path)[4];
  return youtubeId;
}

function isValidYoutubeVideo(link) {
  var match = link.match(/https?:\/\/((.+\.)?youtube.com|youtu.be)\/(.+)/i);
  if(match == null) return false;
  var path = /https?:\/\/((.+\.)?youtube.com|youtu.be)\/(.+)/i.exec(link)[3];
  var pathHasId = path.match(/((^(embed\/)?[a-zA-Z0-9_-]{11}($|\?(.+)?))|(^watch\?(v=([a-zA-Z0-9_-]{11}($|\&.*))|.*(\&v=([a-zA-Z0-9_-]{11}($|(\&.*)))))))/i);
  if(pathHasId == null) return false;  
  return true;
}

function fillDataInForm(data) {
  $("#input-lecture-video-id").val(data["id"]);
  $("#input-lecture-platform").val(data["platform"]);
  $("#input-lecture-title").val(data["title"]);
  var slug = slugify(data["title"]);
  $("#input-lecture-slug").val(slug);
  $("#input-lecture-end").val(parseTime(parseInt(data["start"])+parseInt(data["duration"])));
  $("#input-lecture-description").text(data["description"]);
  $("#new-lecture-thumb").attr("src", data["thumbnail"]["url"]);
  $("#input-lecture-thumbnail").val(data["thumbnail"]["url"]);
  $("#input-lecture-start").val(parseTime(data["start"]));    
  
  $("#input-lecture-end").val(parseTime(parseInt(data["start"])+parseInt(data["duration"]))); 
  
}

function showErrors(errorList) {
    $.each(errorList, function(key, value) {
      $("#new-lecture-errors").append("<p>"+value+"</p>");      
    });
    $("#new-lecture-errors").removeClass("hidden");
}

function showError(error) {
  showErrors([error]);
}

function hideErrors() {  
  $("#new-lecture-errors").html(""); 
  $("#new-lecture-errors").addClass("hidden"); 
}

function parseTime(time) {
  seconds = time % 60;
  if(seconds == time) {
    return "0:"+addZero(seconds);
  } else {
     minutes = parseInt(time / 60)%60;
    if(minutes*60+seconds == time) {
      return minutes +":"+ addZero(seconds);
    } else {
      hours = parseInt(parseInt(time/60)/60);
      return hours+":"+addZero(minutes)+":"+addZero(seconds);
    }
  }
}

$("#input-lecture-slug").focus(function() {
  slugAtBegin = $(this).val();
});

$("#input-lecture-slug").blur(function() {
  if(!slugDidChange) {
    if(slugAtBegin != $(this).val()) {
      slugDidChange = true;
    }    
  }
});

$("#input-lecture-title").keyup(function() {
  if(!slugDidChange)   
    $("#input-lecture-slug").val(slugify($(this).val()));
});

$("form#newLecture").submit(function(e){
  
    $("#new-lecture-errors").addClass("hidden");
    $("#new-lecture-success").addClass("hidden");
    $("#new-lecture-errors").html("");
    
    var category = $("#input-lecture-category").val();
    if(category == "") {
      showError("Please select a category!");
    } else {
      formData = new FormData($(this)[0]);
    
      form = $(this);
      var requestUrl = form.attr("action")+"/"+category;
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
            // Reset Form
            $("#new-lecture-success").removeClass("hidden");
            
            // Close Modal
            $("#create-lecture-modal").modal("hide");
            
            // Show Link to created Video
            $("#new-lecture-link").attr("href", data);
            $("#new-lecture-success").removeAttr("hidden");
            
            resetForm(form);
            
          },
          error : function(e) {
            errorMsgs = e.responseJSON;
            showErrors(errorMsgs);
          },
          done : function(e) {
          }
      });
    }
    e.preventDefault();
});

$("#create-lecture-modal").on('hidden.bs.modal', function (e) {
  resetForm($("form#newLecture"));  
})

$("#create-lecture-modal").on('show.bs.modal', function (e) {
  $("#input-lecture-category").val($("#input-lecture-category").data("default"));
})

function resetForm(form) {
  form.trigger("reset");
  $("#input-lecture-description").text("");
  $("#new-lecture-thumb").removeAttr("src");
  $("#input-lecture-lecturer").val("");
  $("#input-lecture-category").val("");
        
  $("#new-lecture-continue").removeAttr("disabled");
  $("#new-lecture-submit").attr("disabled", "true");
  
  $("#new-lecture-link-form").removeClass("hidden");
  $("#new-lecture-data-form").addClass("hidden");
  
  slugDidChange = false;
  slugAtBegin = "";
}

$("#close-success-alert").click(function() {
  $("#new-lecture-success").addClass("hidden");
});



