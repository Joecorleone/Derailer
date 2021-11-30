package de.badgersburrow.derailer.objects;

public interface SoundListener {
    public void playSoundButton();

    // When users click on a sign
    public void playSoundSign();

    // When an explosion occurs
    public void playSoundExplosion();

    // When an option is turned
    public void playSoundOption();

    // When a switch is changed
    public void playSoundSwitch();

    // When a switch is changed
    public void playSoundToggle();

    // When a dialog is closed
    public void playSoundDialog();

    // a swoshy sound
    public void playSoundSwosh();

    // when a player is dropped on cart
    public void playSoundDrop();

    // when something metap is picked up
    public void playSoundPickup();
}
