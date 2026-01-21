#!/bin/bash
#
# Starts the webapp.
systemctl daemon-reload
systemctl enable ddmsence.service
systemctl restart ddmsence.service