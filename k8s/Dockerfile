# 기본 Jenkins LTS 이미지 사용
FROM jenkins/jenkins:lts

# root 사용자로 전환
USER root

# Docker 설치
RUN apt-get update && \
    apt-get install -y docker.io && \
    rm -rf /var/lib/apt/lists/*

# jenkins 사용자로 전환
USER jenkins
