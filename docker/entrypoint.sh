#!/bin/bash

# Start the run once job.
echo "Docker container has been started"

declare -p | grep -Ev 'BASHOPTS|BASH_VERSINFO|EUID|PPID|SHELLOPTS|UID' > /container.env

# Setup a cron schedule
echo "SHELL=/bin/bash
BASH_ENV=/container.env
0 2 * * 2 /run.sh >> /var/log/cron.log 2>&1
# This extra line makes it a valid cron" > scheduler.txt

crontab scheduler.txt
cron
touch /var/log/cron.log
tail -f /var/log/cron.log