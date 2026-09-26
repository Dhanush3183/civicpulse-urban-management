const AUTH_URL = 'http://localhost:8081/api/auth';
const ISSUE_URL = 'http://localhost:8082/api/issues';
let authToken = '';

// Authentication Logic
async function register() {
    const user = document.getElementById('username').value;
    const pass = document.getElementById('password').value;
    const msg = document.getElementById('auth-msg');

    if (!user || !pass) return msg.innerText = 'Username and password required.';

    try {
        const response = await fetch(`${AUTH_URL}/register`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ username: user, password: pass })
        });
        
        if (response.ok) {
            msg.style.color = 'green';
            msg.innerText = 'Registration successful! Please login.';
        } else {
            msg.innerText = await response.text();
        }
    } catch (err) {
        msg.innerText = 'Auth connection failed.';
    }
}

async function login() {
    const user = document.getElementById('username').value;
    const pass = document.getElementById('password').value;
    const msg = document.getElementById('auth-msg');

    try {
        const response = await fetch(`${AUTH_URL}/login`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ username: user, password: pass })
        });

        if (response.ok) {
            authToken = await response.text();
            document.getElementById('auth-section').style.display = 'none';
            document.getElementById('dashboard-section').style.display = 'block';
            loadIssues(); 
        } else {
            msg.innerText = 'Invalid credentials.';
        }
    } catch (err) {
        msg.innerText = 'Auth connection failed.';
    }
}

// Issue Management Logic
async function reportIssue() {
    const title = document.getElementById('issue-title').value;
    const desc = document.getElementById('issue-desc').value;
    const loc = document.getElementById('issue-loc').value;
    const cat = document.getElementById('issue-cat').value;
    const msg = document.getElementById('dashboard-msg');

    if (!title || !desc || !loc) return msg.innerText = 'Please fill all fields.';

    const payload = { title: title, description: desc, location: loc, category: cat };

    try {
        const response = await fetch(`${ISSUE_URL}/report`, {
            method: 'POST',
            headers: { 
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${authToken}` 
            },
            body: JSON.stringify(payload)
        });

        if (response.ok) {
            msg.style.color = 'green';
            msg.innerText = 'Issue reported successfully!';
            
            document.getElementById('issue-title').value = '';
            document.getElementById('issue-desc').value = '';
            document.getElementById('issue-loc').value = '';
            
            loadIssues(); 
        }
    } catch (err) {
        msg.style.color = '#a41924';
        msg.innerText = 'Failed to submit issue.';
    }
}

async function loadIssues() {
    const listDiv = document.getElementById('issues-list');
    listDiv.innerHTML = 'Loading...';

    try {
        const response = await fetch(`${ISSUE_URL}/all`, {
            headers: { 'Authorization': `Bearer ${authToken}` }
        });
        
        const issues = await response.json();
        listDiv.innerHTML = '';
        
        if(issues.length === 0) {
            listDiv.innerHTML = '<p>No issues reported yet.</p>';
            return;
        }

        issues.reverse().forEach(issue => {
            const date = new Date(issue.reportedAt).toLocaleString();
            listDiv.innerHTML += `
                <div class="issue-item">
                    <h4>${issue.title} - [${issue.category}]</h4>
                    <p>${issue.description}</p>
                    <p><strong>Location:</strong> ${issue.location} | <strong>Reported:</strong> ${date}</p>
                    <span class="status-badge">${issue.status}</span>
                </div>
            `;
        });
    } catch (err) {
        listDiv.innerHTML = '<p style="color:red;">Failed to fetch issues.</p>';
    }
}