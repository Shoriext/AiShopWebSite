FROM ubuntu:latest
LABEL authors="urapo"

ENTRYPOINT ["top", "-b"]