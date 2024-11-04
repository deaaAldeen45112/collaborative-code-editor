import { Component, OnInit, HostListener } from '@angular/core';
import { Location } from '@angular/common';

@Component({
  selector: 'app-restricted-screen',
  templateUrl: './restricted-screen.component.html',
  styleUrls: ['./restricted-screen.component.css']
})
export class RestrictedScreenComponent implements OnInit {
  constructor(private location: Location) {}

  ngOnInit(): void {
    history.replaceState(null, '', location.href);

    //todo: these function used Override the back navigation, or change behaveor
    window.onpopstate = () => {
      this.location.replaceState('/another-route');
    };
  }
}
