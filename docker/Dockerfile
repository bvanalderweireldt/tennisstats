from maven:3-jdk-11-openj9

RUN apt-get update && \
    apt-get -y install git ssh cron

ADD --chown=root:root ssh.tar /root/

COPY gitconfig root/.gitconfig

RUN cd /root/ && \
    eval "$(ssh-agent)" && \
    ssh-add ~/.ssh/github-docker.id_rsa && \
    git clone git@github.com:bvanalderweireldt/tennisstats.git


ADD run.sh ../run.sh
ADD entrypoint.sh /entrypoint.sh

RUN chmod +x /run.sh /entrypoint.sh

ENTRYPOINT /entrypoint.sh