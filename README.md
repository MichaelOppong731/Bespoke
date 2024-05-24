
Bespoke Video Streaming Application

Introduction

The Bespoke Video Streaming Application is a web-based platform for uploading, managing, and streaming videos. Users can upload videos to an AWS S3 bucket, and stream them directly from the application. This project is built using Spring Boot for the backend, Amazon S3 for storage, and HTML, CSS, and JavaScript for the frontend.

Features

•	Upload videos to AWS S3
•	Stream videos directly from the application
•	View video details (title and description)
•	Navigate between videos
•	Share video links

 Technologies Used

•	Backend: Spring Boot, Spring MVC, Spring Data JPA
•	Frontend: HTML, CSS, JavaScript
•	Database: Amazon RDS PostgreSQL Database (for development)
•	Storage:  Amazon S3
•	Build Tool: Maven

Prerequisites

•	Java 17 or later
•	Maven 3.6+
•	An AWS account with S3 access
•	Basic knowledge of Spring Boot and AWS S3

Getting Started

Clone the Repository

git clone https://github.com/MichaelOppong731/Bespoke.git
cd bespoke-video-streaming

Configuration

Create an `application.properties` file in the `src/main/resources` directory and add your AWS S3 credentials and bucket name:

properties
aws.s3.bucket.name=your-bucket-name
aws.accessKeyId=your-access-key-id
aws.secretKey=your-secret-key

Build and Run


mvn clean install
mvn spring-boot:run

The application will start on `http://localhost:8080`.

Endpoints

Upload Video

•	URL: `/upload`
•	Method: `GET`
•	Description: Returns the upload video page.

•	URL: `/upload`
•	Method: `POST`
•	Parameters:
•	`title` (String): The title of the video.
•	`description` (String): The description of the video.
•	`file` (MultipartFile): The video file.
•	Description: Uploads a video to AWS S3 and saves the video details in the database.

Stream Video

•	URL: `/video/{id}/play`
•	Method: `GET`
•	Produces: `application/octet-stream`
•	Description: Streams the video with the given ID.

Get All Videos

•	URL: `/api/v1/videos`
•	Method: `GET`
•	Description: Returns a list of all videos.

Troubleshooting

•	Ensure your AWS credentials and bucket name are correctly configured in the `application.properties` file.
•	Check the browser console for any errors related to missing elements or network issues.
•	Verify that the backend server is running and accessible at `http://localhost:8080`.


