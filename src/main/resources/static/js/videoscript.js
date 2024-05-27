// scripts.js

// Share button
function copyVideoLink() {
    let currentVideo = availableVideos[currentIndex];
    // Build the link to the video page, not the playback endpoint
    let videoLink = window.location.origin + '/videos/' + currentVideo.id;

    navigator.clipboard.writeText(videoLink).then(function() {
        alert('Video page link copied to clipboard!');
    }, function() {
        alert('Failed to copy video link.');
    });
}


// Attaching the event listener to the share button
document.querySelector('.share-button').addEventListener('click', copyVideoLink);

let currentIndex = 0;
let availableVideos = [];

// Function to fetch and store videos once
function fetchVideos() {
    return fetch('/api/v1/videos')
        .then(response => {
            if (!response.ok) {
                throw new Error('Failed to fetch videos');
            }
            return response.json();
        })
        .then(data => {
            availableVideos = data;
        })
        .catch(error => {
            console.error("Error fetching videos:", error);
            throw error;
        });
}

function nextVideo() {
    if (availableVideos.length === 0) {
        fetchVideos();
        return;
    }

    currentIndex = (currentIndex + 1) % availableVideos.length;
    updateVideoDisplay();
}

function previousVideo() {
    if (currentIndex === 0) {
        return;
    }

    currentIndex = (currentIndex - 1) % availableVideos.length;
    updateVideoDisplay();
}

// Function to update video details and player
function updateVideoDisplay() {
    const videoInfoTitle = document.querySelector('.video-info__title');
    const videoInfoDescription = document.querySelector('.video-info__description');
    const videoPlayer = document.getElementById('mainVideoPlayer');

    if (availableVideos.length > 0) {
        const currentVideo = availableVideos[currentIndex];
        videoInfoTitle.textContent = currentVideo.title;
        videoInfoDescription.textContent = currentVideo.description;
        videoPlayer.src = `/video/${currentVideo.id}/play`;
        videoPlayer.load();
        videoPlayer.style.display = 'block';
    } else {
        videoInfoTitle.textContent = "No videos available.";
        videoInfoDescription.textContent = "";
        videoPlayer.style.display = 'none';
    }

    updateButtonVisibility();
}

// Function to control button visibility
function updateButtonVisibility() {
    let nextButton = document.querySelector('.video-navigation__button:last-child');
    let prevButton = document.querySelector('.video-navigation__button:first-child');

    if (availableVideos.length === 0) {
        nextButton.style.display = 'none';
        prevButton.style.display = 'none';
    } else {
        nextButton.style.display = (currentIndex === availableVideos.length - 1) ? 'none' : 'block';
        prevButton.style.display = (currentIndex === 0) ? 'none' : 'block';
    }
}

// Function to get video ID from URL
function getVideoIdFromUrl() {
    let path = window.location.pathname;
    let pathParts = path.split('/');
    return pathParts[pathParts.length - 1];
}


function displayFirstVideo() {
    const videoId = getVideoIdFromUrl();

    fetchVideos().then(() => {
        if (videoId) { // Check if videoId exists in the URL
            currentIndex = availableVideos.findIndex(video => video.id == videoId);
            if (currentIndex === -1) { // Check if video was not found
                console.error("Video not found");
                // Handle case where the video is not found (e.g., show an error message)
                currentIndex = 0; // Reset currentIndex to 0
            }
        } else if (availableVideos.length > 0) {
            currentIndex = 0;
        }
        updateVideoDisplay();
        updateButtonVisibility();
    }).catch(error => {
        console.error("Failed to fetch videos:", error);
        // Handle error, e.g., display an error message to the user
    });
}




// Add the event listener for when the page loads
document.addEventListener('DOMContentLoaded', function() {
    displayFirstVideo();
});