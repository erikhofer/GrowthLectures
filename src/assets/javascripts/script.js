var API_KEY = "AIzaSyBlWQkVfTva6wY8wey_CZY8qE8WZhhXmnA";

$(function() {
  // Set CSRF header for all ajax requests
  var csrfHeader = $("meta[name='_csrf_header']").attr("content");
  var csrfToken = $("meta[name='_csrf']").attr("content");
  $.ajaxSetup({
    beforeSend: function(xhr) {
      xhr.setRequestHeader(csrfHeader, csrfToken);
    }
  });
  
  $(".lecture-summary").each(function() {
    var platform = $(this).data("platform");
    
    var data = null;
    if(platform == "YOUTUBE") {
      var youtubeId = $(this).find(".lecture-data").data("youtubeid");
      data = getYoutubeVideoData(youtubeId);
    }
    if(data != null) {
      $(this).find(" .external-information-views").text(data["views"]);
      $(this).find(" .external-information-likes").text(data["likes"]);
      $(this).find(" .external-information-dislikes").text(data["dislikes"]);
      $(this).find(".thumbnail img").attr("src", data["thumbnail"]["url"]);
    }
    
    $(this).find(".community-rating > input, .user-rating > input").rating({
      size:'xs',
      displayOnly: true
    });
  });
});

function getYoutubeVideoData(youtubeId) {

  var requestUrl = "https://www.googleapis.com/youtube/v3/videos?id="+youtubeId+"&key="+API_KEY+"&part=snippet,contentDetails,statistics";
  var returnData = {};
  $.ajax({
    url: requestUrl,
    dataType: 'json',
    async: false,
    success: function(data) {
      isVideo = data["pageInfo"]["totalResults"];
      if(isVideo) {
        item = data["items"][0];
        
        snippet = item["snippet"];
        details = item["contentDetails"];
        
        returnData["id"] = youtubeId;
        
        returnData["title"] = snippet["title"];
        returnData["description"] = snippet["description"];
        
        returnData["channelTitle"] = item["snippet"]["channelTitle"];   
        returnData["channelLink"] = "https://www.youtube.com/channel/"+item["snippet"]["channelId"];  
           
        returnData["likes"] = parseInt(item["statistics"]["likeCount"]).toLocaleString();    
        returnData["dislikes"] = parseInt(item["statistics"]["dislikeCount"]).toLocaleString();
        returnData["views"] = parseInt(item["statistics"]["viewCount"]).toLocaleString();
        
        returnData["link"] = "http://www.youtube.com/watch?v="+youtubeId;
        returnData["platform"] = "YOUTUBE";
        
        // Extract Thumbnail
        thumbnails = snippet["thumbnails"]
        thumb = thumbnails["maxres"];
        if(thumb == null) {
          thumb = thumbnails["medium"];
        }
        if(thumb == null) {
          thumb = thumbnails["default"];
        }
        returnData["thumbnail"] = thumb;
      
        // Extract Druation
        duration = details["duration"];
        durationElements = /PT((\d{0,3})H)?((\d{0,2})M)?((\d{0,2})S)?/ig.exec(duration);
         
        hours = durationElements[2];
        minutes = durationElements[4];
        seconds = durationElements[6];
         
        durationInSeconds = parseInt(seconds);
        if(minutes) durationInSeconds += parseInt(minutes)*60;
        if(hours) durationInSeconds += parseInt(hours)*60*60;
         
        returnData["duration"] = durationInSeconds;
        
        return;
        
      } else {
        returnData = null;
        return;
      }
    }
  });
  return returnData;
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

function addZero(i) {
  if(i<10) return "0"+i;
  return i;
}