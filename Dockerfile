FROM ubuntu:latest
LABEL authors="sahar"

ENTRYPOINT ["top", "-b"]