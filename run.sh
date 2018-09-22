#!/bin/bash

cd python
gunicorn wsgi:app