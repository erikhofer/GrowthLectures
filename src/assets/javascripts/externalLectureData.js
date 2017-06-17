$(function() {
  
  var platform = $(".lecture-video-frame").data("media-platform");
  switch(platform) {
    case "YOUTUBE": loadYoutubeDataOnLecturePage(); break;
    default: break;
  }
  
  function loadYoutubeDataOnLecturePage() {
    var youtubeId = $(".lecture-video-frame iframe").data("youtube-id");
    if (youtubeId != null && youtubeId != '') {
      var videoData = getYoutubeVideoData(youtubeId);
      
      $("#external-information-source").text("Youtube");

      $("#external-information-video-link").attr("href", videoData["link"]);
      $("#external-information-likes").text(videoData["likes"]);
      $("#external-information-dislikes").text(videoData["dislikes"]);
      $("#external-information-views").text(videoData["views"]);
      
      $("#external-information-channel-title").text(videoData["channelTitle"]);
      $("#external-information-channel-title").attr("href", videoData["channelLink"]);
       
    }    
  }
     
});