var API_KEY = "AIzaSyBlWQkVfTva6wY8wey_CZY8qE8WZhhXmnA";

var YOUTUBE_EXP = /https?:\/\/((.+\.)?youtube.com|youtu.be)\/(.+)/i;
var VIMEO_EXP = /https?:\/\/(.*\.)?vimeo.com\/(video\/)?(\d+)(\?(.*))?/i;

var slugDidChange = false;

$("#new-lecture-continue").click(function() {
  
  hideErrors();
  slugDidChange = false;
  
  var link = $("#input-lecture-link").val();
  if(link.match(YOUTUBE_EXP)) {
    var data;
    if(isValidYoutubeVideo(link)) {
      data = getYoutubeVideoData(link);
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
  
  if(data != null && data["title"] != "") {
    
    console.log(data);
    fillDataInForm(data);
    $("#new-lecture-link-form").addClass("hidden");
    $("#new-lecture-data-form").removeClass("hidden");
    
    $("#new-lecture-continue").attr("disabled", "true");
    $("#new-lecture-submit").removeAttr("disabled");
  } else {
    showError("Video could not be found!");
    return
  }
});

function getYoutubeVideoData(link) {
  
  var path = /https?:\/\/((.+\.)?youtube.com|youtu.be)\/(.+)/i.exec(link)[3];
  var youtubeId = /((\?|\&)(v)=|^|embed\/)([a-zA-Z0-9-_]{11})/.exec(path)[4];
  
  var requestUrl = "https://www.googleapis.com/youtube/v3/videos?id="+youtubeId+"&key="+API_KEY+"&part=snippet,contentDetails,statistics";
  var returnData = {};
  $.ajax({
    url: requestUrl,
    dataType: 'json',
    async: false,
    success: function(data) {
      isVideo = data["pageInfo"]["totalResults"];
      if(isVideo) {
        items = data["items"][0];
  
        snippet = items["snippet"];
        returnData["title"] = snippet["title"];
        returnData["description"] = snippet["description"];
        returnData["id"] = youtubeId;
        returnData["platform"] = "youtube";
          
        thumbnails = snippet["thumbnails"]
        thumb = thumbnails["maxres"];
        if(thumb == null) {
          thumb = thumbnails["medium"];
        }
        if(thumb == null) {
          thumb = thumbnails["default"];
        }
        returnData["thumbnail"] = thumb;
        returnData["url"] = generateYoutubeUrl(youtubeId);
        
        var start = /(\?|\&)(start|t)=(\d+)/i.exec(path);
        var end = /(\?|\&)(end)=(\d+)/i.exec(path);      
        
        details = items["contentDetails"];
        duration = details["duration"];
        
        durationElements = /PT((\d{0,3})H)?((\d{0,2})M)?((\d{0,2})S)?/ig.exec(duration);
         
        hours = durationElements[2];
        minutes = durationElements[4];
        seconds = durationElements[6];
         
        durationInSeconds = parseInt(seconds);
        if(minutes) durationInSeconds += parseInt(minutes)*60;
        if(hours) durationInSeconds += parseInt(hours)*60*60;
         
        if(start == null) {
          returnData["start"] = 0;
        } else {
          console.log(start);
          returnData["start"] = start[3];
        }
        if(end == null) {
          returnData["duration"] = durationInSeconds;
        } else {
          console.log(end);
          returnData["duration"] = end[3]-returnData["start"];
        }
        return;
        
      } else {
        returnData = null;
        return;
      }
    }
  });
  return returnData;
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
  $("#input-lecture-start").val(parseTime(data["start"]));
  $("#input-lecture-end").val(parseTime(parseInt(data["start"])+parseInt(data["duration"])));
  $("#input-lecture-description").text(data["description"]);
  $("#new-lecture-thumb").attr("src", data["thumbnail"]["url"]);
}

function generateYoutubeUrl(youtubeId) {
  return "https://www.youtube.com/watch?v="+youtubeId;
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

// Source: https://gist.github.com/mathewbyrne/1280286
function slugify(text)
{
  return text.toString().toLowerCase()
    .replace(/\s+/g, '-')           // Replace spaces with -
    .replace(/[^\w\-]+/g, '')       // Remove all non-word chars
    .replace(/\-\-+/g, '-')         // Replace multiple - with single -
    .replace(/^-+/, '')             // Trim - from start of text
    .replace(/-+$/, '');            // Trim - from end of text
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

function addZero(i) {
  if(i<10) return "0"+i;
  return i;
}

$("#input-lecture-slug").blur(function() {
  slugDidChange = true;
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
            console.log(e);
            showErrors(errorMsgs);
          },
          done : function(e) {
            console.log("DONE");
          }
      });
    }
    e.preventDefault();
});

$("#create-lecture-modal").on('hidden.bs.modal', function (e) {
  resetForm($("form#newLecture"));  
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
}

$("#close-success-alert").click(function() {
  $("#new-lecture-success").addClass("hidden");
});



