# JupyterHub

[JupyterHub](https://github.com/jupyterhub/jupyterhub) is the best way to serve [Jupyter notebook](https://jupyter-notebook.readthedocs.io/en/latest/) for multiple users. Because JupyterHub manages a separate Jupyter environment for each user, it can be used in a class of students, a corporate data science group, or a scientific research group. It is a multi-user **Hub** that spawns, manages, and proxies multiple instances of the single-user [Jupyter notebook](https://jupyter-notebook.readthedocs.io/en/latest/) server.

## Distributions

JupyterHub can be used in a collaborative environment by both small (0-100 users) and large teams (more than 100 users) such as a class of students, corporate data science group or scientific research group. It has two main distributions which are developed to serve the needs of each of these teams respectively.

1. [The Littlest JupyterHub](https://github.com/jupyterhub/the-littlest-jupyterhub) distribution is suitable if you need a small number of users (1-100) and a single server with a simple environment.
2. [Zero to JupyterHub with Kubernetes](https://github.com/jupyterhub/zero-to-jupyterhub-k8s) allows you to deploy dynamic servers on the cloud if you need even more users. This distribution runs JupyterHub on top of [Kubernetes](https://kubernetes.io/).

## Subsystems

JupyterHub is made up of four subsystems

- a **Hub** (tornado process) that is the heart of JupyterHub
- a **configurable http proxy** (node-http-proxy) that receives the requests from the client’s browser
- multiple **single-user Jupyter notebook servers** (Python/IPython/tornado) that are monitored by Spawners
- an **authentication class** that manages how users can access the system

Additionally, optional configurations can be added through a `config.py` file and manage users kernels on an admin panel. A simplification of the whole system is displayed in the figure below:

[![JupyterHub subsystems](https://jupyterhub.readthedocs.io/en/stable/_images/jhub-fluxogram.jpeg)](https://jupyterhub.readthedocs.io/en/stable/_images/jhub-fluxogram.jpeg)

JupyterHub performs the following functions:

- The Hub launches a proxy
- The proxy forwards all requests to the Hub by default
- The Hub handles user login and spawns single-user servers on demand
- The Hub configures the proxy to forward URL prefixes to the single-user notebook servers

For convenient administration of the Hub, its users, and services, JupyterHub also provides a [REST API](https://jupyterhub.readthedocs.io/en/stable/reference/rest-api.html).

The JupyterHub team and Project Jupyter value our community, and JupyterHub follows the Jupyter [Community Guides](https://jupyter.readthedocs.io/en/latest/community/content-community.html).