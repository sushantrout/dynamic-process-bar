import { Component, NgZone } from '@angular/core';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
})
export class AppComponent {
  constructor(private zone: NgZone) {
  }

  progress = 0;

  incrementProgress() {
    this.getEventData().subscribe((data : any) => {
      this.zone.run(() => {
        this.progress = parseInt(JSON.parse(data)['current']);
      });
    });
  }

  getEventData() {
    let eventSource = new EventSource('http://localhost:8080/api/processor');
    return new Observable<string>((observer) => {
      eventSource.onmessage = (event) => {
        observer.next(event.data);
      };

      eventSource.onerror = (error) => {
        observer.error('EventSource failed: ' + error);
        eventSource.close();
      };
      return () => eventSource.close();
    });
  }
}
