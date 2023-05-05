{% include "housekeeping/base/header.tpl" %}
  <body>
    {% set badgesActive = " active" %}
	{% include "housekeeping/base/navigation.tpl" %}
     <h1 class="mt-4">Edit Room Badges</h1>
		{% include "housekeeping/base/alert.tpl" %}
		<p>Edit all the room badges that are given when entering the room.</p>
		<p><a href="/{{ site.housekeepingPath }}/badges/create?id={{ id }}" class="btn btn-danger">New Badge</a></p>
          <div class="table-responsive">
		    <form method="post">
            <table class="table table-striped">
              <thead>
                <tr>
                  <th>Code</th>
				  <th>Badge Name</th>
				  <th>Badge Description</th>
				  <th>Preview</th>
                  <th>Users</th>
				  <th></th>
                </tr>
              </thead>
              <tbody>
			  {% for badge in badges %}
				{% set id = badge.getBadgeCode() %}

				<input type="hidden" name="badge-id-{{ id }}" value="{{ id }}">
                <tr>
				  <td>
						<input type="text" name="badgecode-{{ id }}" class="form-control" id="searchFor" value="{{ badge.getBadgeCode() }}">
				  </td>

				  <td>
						<input type="text" name="badgename-{{ id }}" class="form-control" id="searchFor" value="{{ badge.getBadgeName()  }}">
				  </td>
				  <td>
						<input type="text" name="badgedescription-{{ id }}" class="form-control" id="searchFor" value="{{ badge.getBadgeDescription() }}">
				  </td>
				  <td>
						<img src="{{ site.staticContentPath }}/c_images/album1584/{{ badge.getBadgeCode() }}.gif">
				  </td>
				  <td>
						<input type="text" disabled class="form-control" id="searchFor" value="{{ badge.getUsers() }}">
				  </td>
				  <td>
						<a href="/{{ site.housekeepingPath }}/badges/wipe?id={{ id }}" class="btn btn-info">Wipe</a>
				  </td>
                  </tr>
			   {% endfor %}
              </tbody>
            </table>
			<div class="form-group">
				<button type="submit" class="btn btn-info">Save Badges</button>
			</div>
		</form>
      </div>
    </div>
  </div>
  <script src="https://code.jquery.com/jquery-3.1.1.slim.min.js"></script>
  <script src="https://blackrockdigital.github.io/startbootstrap-simple-sidebar/vendor/bootstrap/js/bootstrap.bundle.min.js"></script>
  <script>
    $("#menu-toggle").click(function(e) {
      e.preventDefault();
      $("#wrapper").toggleClass("toggled");
    });
  </script>
</body>
</html>

{% include "housekeeping/base/footer.tpl" %}