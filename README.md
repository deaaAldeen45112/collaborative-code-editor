# Collaborative Code Editor

The Collaborative Code Editor is a web-based platform that enables multiple users to edit, execute, and review code collaboratively in real-time. Built with a robust backend using Java (Spring Boot), Angular frontend, and Docker for code execution, this editor supports version control, real-time communication, role-based access control, and CI/CD practices, making it a powerful tool for collaborative development.

## Table of Contents
- [Objectives](#objectives)
- [Features](#features)
- [Technologies Used](#technologies-used)
- [Video Demo](#video-demo)
- [DevOps and Deployment](#devops-and-deployment)
- [Project Report](#project-report)

---

## Objectives

The primary objectives of the Collaborative Code Editor project are as follows:

1. **Real-Time Collaboration**  
   Enable a multi-user environment where changes made by any user are instantly reflected across all sessions in real-time.

2. **Real-Time Versioning**  
   Implement advanced version control with real-time tracking, user contribution logs, and the ability to revert to previous versions, ensuring reliability and data integrity across collaborative sessions.

3. **Code Execution**  
   Support code execution in multiple programming languages directly in the editor, with real-time display of output and error messages for a seamless development experience.

4. **Code Reviews and Comments**  
   Allow users to leave comments on specific lines of code, facilitating collaborative code review and discussion, ultimately enhancing code quality and team learning.

## Features

- **Real-Time Collaborative Editing**: Synchronized editing via WebSocket with HTTP polling for manual refresh.
- **Role-Based Access Control (RBAC)**: User roles (Admin, Editor, Viewer) manage access and editing permissions.
- **Real-Time Version Control**: Tracks changes with detailed logs and reversion capabilities to allow collaborative versioning.
- **Code Execution**: Multi-language support using Docker containers.
- **Code Review and Comments**: Inline commenting for collaborative review.
- **OAuth Authentication**: Supports Google and GitHub login for secure access.
- **Secure Communication**: HTTPS and WebSocket encryption with Nginx as a reverse proxy.

## Technologies Used

- **Backend**: Java (Spring Boot)
- **Frontend**: Angular, HTML, CSS, JavaScript
- **Database**: MySQL
- **Authentication**: OAuth 2.0 (Google/GitHub)
- **Real-Time Communication**: WebSocket and HTTP polling
- **Containerization**: Docker
- **Web Server**: Nginx
- **CI/CD**: GitHub Actions
- **Cloud Hosting**: AWS (for deployment)

## Video Demo

Watch the [video demo](https://youtu.be/uyPt7ksE2bQ) to see the project in action.

## DevOps and Deployment

The DevOps setup for this project ensures scalability, ease of deployment, and secure communication:

- **CI/CD Pipeline**: A GitHub Actions workflow automates the CI/CD process, including build, test, and deployment stages.
- **Containerization with Docker**: Docker containers encapsulate the code execution environments, providing isolated and secure runtime environments for each supported language.
- **AWS Deployment**: The application is deployed on AWS, ensuring scalability and reliability.
- **Nginx Reverse Proxy**: Nginx is configured as a reverse proxy for handling HTTP and WebSocket requests, managing SSL termination, and ensuring secure communication between clients and the backend.
- **SSL Setup**: SSL/TLS is configured in Nginx to secure HTTP and WebSocket communications, enhancing data privacy and security.

## Project Report

For a detailed overview of the design and implementation of the Collaborative Code Editor, please refer to the [Project Report](https://github.com/deaaAldeen45112/collaborative-code-editor/blob/main/DeyaAldeen_ProjectReport.pdf).

---

This document provides a comprehensive overview of the Collaborative Code Editor project, its objectives, features, and technical implementation. For further details, please refer to the source code and documentation in the repository.
