<nav class="sidebar sidebar-offcanvas" id="sidebar" style="position: fixed; height: 250px; overflow: auto;">
    <ul class="nav">
        <li class="nav-item nav-profile">
            <div class="nav-link">
                <div class="user-wrapper">
                    <div class="profile-image">
                        <asset:image src="faces/face1.jpg" alt="profile image"></asset:image>
                    </div>
                    <div class="text-wrapper">
                        <p class="profile-name">${username}</p>
                        <div>
                            <small class="designation text-muted">Manager</small>
                            <span class="status-indicator online"></span>
                        </div>
                    </div>
                </div>
                <g:link controller="home" action="newProject" class="btn btn-success btn-block">Nouveau projet
                    <i class="mdi mdi-plus"></i>
                </g:link>
            </div>
        </li>
        <li class="nav-item">
            <a class="nav-link" href="${request.contextPath}/">
                <i class="menu-icon mdi mdi-television"></i>
                <span class="menu-title">Dashboard</span>
            </a>
        </li>
        <li class="nav-item">
            <g:link controller="home" action="projets" class="nav-link">
                <i class="menu-icon mdi mdi-table"></i>
                <span class="menu-title">Projets</span>
            </g:link>
        </li>
        <li class="nav-item">
            <g:link controller="home" action="bpms" class="nav-link">
                <i class="menu-icon mdi mdi-table"></i>
                <span class="menu-title">BPM</span>
            </g:link>
        </li>
    </ul>
</nav>
